package stream.okchun.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.database.entity.billing.BillingAccount;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.repos.billing.*;
import stream.okchun.dashboard.service.billing.tx.BillingAccountType;
import stream.okchun.dashboard.service.billing.tx.BillingTransactionInstance;
import stream.okchun.dashboard.service.billing.tx.TransactionType;
import stream.okchun.dashboard.service.billing.invoice.BillingInvoiceInstance;
import stream.okchun.dashboard.service.billing.pg.PaymentGateway;

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

	public BillingAccount getBillingAccount(BillingAccountType type, long account_ref_id, String currency) {
		return billingAccountRepository.findByAccountTypeAndAccountRef(type.name(), account_ref_id);
	}

	public BillingAccount createBillingAccount(BillingAccountType type, long account_ref_id, String currency,
											   String comment) {
		var account = BillingAccount.builder().currency(currency).balance(BigDecimal.valueOf(0)).accountType(
				type.name()).accountRef(account_ref_id).systemComment(comment).build();
		return billingAccountRepository.save(account);
	}

	/// Transaction을 시작하려면 transaction_prepare를 만들어야 함.
	/// transaction_prepare를 만들고 실제 Tx를 담당하는 객체 생성
	public BillingTransactionInstance declareTransaction(TransactionType tx_type, String tx_name, String prep_log,
														 BillingAccount account) {
		var prep = TransactionPrepare.builder()
				.txType(tx_type.name())
				.txName(tx_name)
				.txPrepLog(prep_log)
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

	public BillingInvoiceInstance declareInvoice(long amount, BillingAccount account, long organizationId,
												 long memberId) {
		// 실제 로직은 아래와 같이 가야하는데, 우리는 one-click 로직이기 때문에, 여기서 다함
		// 1. 결제 확인서 (invoice 생성 + coupon 적용 등등의 단계)
		// 2. 결제 확인서 페이지에서 "결제하기 버튼" 클릭시 transaction_prepare가 생김 (API 이슈로 여러번 응답이 올 수도 있음. 멱등성 땜에 id 할당용)
		// 3. PG에서 commit 신호가 왔을때 transaction을 적용. 이때, invoice와 transaction상의 비용이 다르면 관리자에게 알림을 보내는 로직 필요
		return null;
	}
}