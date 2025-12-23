package stream.okchun.dashboard.service;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.billing.BillingAccount;
import stream.okchun.dashboard.database.entity.billing.BillingInvoice;
import stream.okchun.dashboard.database.entity.billing.LedgerEntry;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.entity.org.Organization;
import stream.okchun.dashboard.database.repos.billing.*;
import stream.okchun.dashboard.exception.billing.TransactionException;
import stream.okchun.dashboard.service.billing.pg.InvoiceCreatedResult;
import stream.okchun.dashboard.service.billing.pg.PaymentGateway;
import stream.okchun.dashboard.service.billing.tx.BillingAccountType;
import stream.okchun.dashboard.service.billing.tx.BillingTransactionInstance;
import stream.okchun.dashboard.service.billing.tx.TransactionType;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {
	private final BillingAccountRepository billingAccountRepository;
	private final TransactionRepository transactionRepository;
	private final TransactionPrepareRepository transactionPrepareRepository;
	private final LedgerEntryLinkRepository ledgerEntryLinkRepository;
	private final LedgerEntryRepository ledgerEntryRepository;
	private final PaymentGateway paymentGateway;
	private final BillingInvoiceRepository billingInvoiceRepository;
	private final CurrencyExchangeService currencyExchangeService;

	public List<BillingAccount> getBillingAccount(BillingAccountType type, long account_ref_id,
												  @Nullable String currency) {
		if (currency == null) {
			return billingAccountRepository.findAllByAccountTypeAndAccountRef(type.name(), account_ref_id);
		}

		return billingAccountRepository.findByAccountTypeAndAccountRefAndCurrency(type.name(), account_ref_id,
				currency).map(List::of).orElse(List.of());
	}

	public Page<BillingInvoice> listBillingInvoice(long org_id, @Nullable Integer page) {
		var pageReq = PageRequest.of(page == null ? 0 : page, 50, Sort.by(Sort.Order.desc(
				"id")));
		return billingInvoiceRepository.findAllByOrgId(org_id, pageReq);
	}

	public BillingAccount createBillingAccount(BillingAccountType type, long account_ref_id, String currency,
											   String comment) {
		var account = BillingAccount.builder().currency(currency).balance(BigDecimal.valueOf(0)).accountType(
				type.name()).accountRef(account_ref_id).systemComment(comment).build();
		return billingAccountRepository.save(account);
	}

	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public InvoiceCreatedResult declareInvoice(long amount, BillingAccount account, Organization org,
											   User user, String invoiceName, String invoiceDescription) {

		var currency = account.getCurrency().trim();

		BillingInvoice invoice = BillingInvoice.builder()
				.org(org)
				.user(user)
				.status("START")
				.txId(null)
				.name(invoiceName)
				.totalPayAmount(BigDecimal.valueOf(amount))
				.currency(currency)
				.account(account)
				.build();
		invoice = billingInvoiceRepository.save(invoice);

		var pgResult = paymentGateway.createInvoice(currency, amount, invoice.getId().toString(),
				invoiceName + "(" + invoiceDescription + ")", null);

		invoice.setPgRef(pgResult.payment_name());
		invoice.setPgRefId(pgResult.payment_id());
		invoice.setPgLog(pgResult.log_data());
		invoice.setStatus("PG_CREATE");

		if (pgResult.success()) {
			var txPrepare = declareTransaction(TransactionType.CREDIT_TOPUP, invoiceName, "", account);
			invoice.setTxId(txPrepare.getTxId());
		} else {
			invoice.setStatus("PG_CREATE_FAIL");
		}

		billingInvoiceRepository.save(invoice);
		return pgResult;
	}

	/// Transaction을 시작하려면 transaction_prepare를 만들어야 함.
	/// transaction_prepare를 만들고 실제 Tx를 담당하는 객체 생성
	public BillingTransactionInstance declareTransaction(TransactionType tx_type, String tx_name,
														 String prep_log, BillingAccount account) {
		var prep = TransactionPrepare.builder()
				.txType(tx_type.name())
				.txName(tx_name)
				.txComment(prep_log)
				.status("START")
				.issuedBy(account)
				.build();
		prep = transactionPrepareRepository.save(prep);

		return BillingTransactionInstance.builder()
				.billingAccountRepository(billingAccountRepository)
				.transactionRepository(transactionRepository)
				.transactionPrepareRepository(transactionPrepareRepository)
				.ledgerEntryLinkRepository(ledgerEntryLinkRepository)
				.ledgerEntryRepository(ledgerEntryRepository)
				.prep(prep)
				.build();
	}

	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void invoicePaymentSuccess(long invoice_id) {
		var invoice = this.billingInvoiceRepository.findById(invoice_id).orElseThrow(
				() -> TransactionException.INVOICE_NOT_FOUND("invoice not found id: " + invoice_id, null,
						null, null));
		var tx = getTransaction(invoice.getTxId());

		var account_pg = billingAccountRepository.findByAccountTypeAndAccountRefAndCurrency("PG",
				paymentGateway.getPGNumber(), invoice.getCurrency()).orElseThrow(
				() -> TransactionException.NO_BILLING_ACCOUNT_FOUND("billing acc: PG /" + Long.toString(
								paymentGateway.getPGNumber()) + invoice.getCurrency(), invoice.getTxId(), null,
						null));
		var account_target = billingAccountRepository.findByAccountTypeAndAccountRefAndCurrency("ORG",
				invoice.getAccount().getId(), invoice.getCurrency()).orElseThrow(
				() -> TransactionException.NO_BILLING_ACCOUNT_FOUND("billing acc: ORG /" + invoice.getAccount()
								.getId() + invoice.getCurrency(), invoice.getTxId(), null,
						null));


		tx.getCreditLedgerEntry().addLedgerEntry(LedgerEntry.builder().account(account_pg)
//				.side("C")
				.amount(invoice.getTotalPayAmount()).comment("invoice - " + invoice_id).build());

		tx.getDebitLedgerEntry().addLedgerEntry(LedgerEntry.builder().account(account_target)
//				.side("D")
				.amount(invoice.getTotalPayAmount()).comment("invoice - " + invoice_id).build());

		tx.commit();
		invoice.setStatus("SUCCESS");
	}

	public BillingTransactionInstance getTransaction(long TxId) {
		var prep = transactionPrepareRepository.findById(TxId).orElseThrow(
				() -> new RuntimeException("tx 발견 실패"));

		return BillingTransactionInstance.builder()
				.billingAccountRepository(billingAccountRepository)
				.transactionRepository(transactionRepository)
				.transactionPrepareRepository(transactionPrepareRepository)
				.ledgerEntryLinkRepository(ledgerEntryLinkRepository)
				.ledgerEntryRepository(ledgerEntryRepository)
				.exchangeService(currencyExchangeService)
				.prep(prep)
				.build();

	}
}