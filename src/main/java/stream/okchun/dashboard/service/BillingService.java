package stream.okchun.dashboard.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.billing.BillingAccount;
import stream.okchun.dashboard.database.entity.billing.BillingInvoice;
import stream.okchun.dashboard.database.entity.billing.LedgerEntry;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.entity.org.Organization;
import stream.okchun.dashboard.database.repos.billing.*;
import stream.okchun.dashboard.service.billing.pg.InvoiceCreatedResult;
import stream.okchun.dashboard.service.billing.pg.PaymentGateway;
import stream.okchun.dashboard.service.billing.tx.BillingAccountType;
import stream.okchun.dashboard.service.billing.tx.BillingTransactionInstance;
import stream.okchun.dashboard.service.billing.tx.TransactionType;

import java.math.BigDecimal;

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

	public BillingAccount getBillingAccount(BillingAccountType type, long account_ref_id, String currency) {
		return billingAccountRepository.findByAccountTypeAndAccountRef(type.name(), account_ref_id);
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
				.name(
						invoiceName)
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
				() -> new RuntimeException("invoice not found"));
		var tx = getTransaction(invoice.getTxId());

		var account_pg = billingAccountRepository.findByAccountTypeAndAccountRef("PG",
				paymentGateway.getPGNumber());
		var account_target = billingAccountRepository.findByAccountTypeAndAccountRef("ORG",
				invoice.getAccount().getId());


		tx.getCreditLedgerEntry().addLedgerEntry(LedgerEntry.builder()
				.account(account_pg)
//				.side("C")
				.amount(invoice.getTotalPayAmount())
				.comment("invoice - " + invoice_id)
				.build());

		tx.getDebitLedgerEntry().addLedgerEntry(LedgerEntry.builder()
				.account(account_target)
//				.side("D")
				.amount(invoice.getTotalPayAmount())
				.comment("invoice - " + invoice_id)
				.build());

		tx.commit();

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