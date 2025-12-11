package stream.okchun.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.database.entity.billing.BillingAccount;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.repos.billing.*;
import stream.okchun.dashboard.service.billing.BillingAccountType;
import stream.okchun.dashboard.service.billing.BillingTransactionManager;
import stream.okchun.dashboard.service.billing.TransactionType;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BillingService {
	private final BillingAccountRepository billingAccountRepository;
	private final TransactionRepository transactionRepository;
	private final TransactionPrepareRepository transactionPrepareRepository;
	private final LedgerEntryLinkRepository ledgerEntryLinkRepository;
	private final LedgerEntryRepository ledgerEntryRepository;

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
	public BillingTransactionManager declareTransaction(TransactionType tx_type, String tx_name, String prep_log,
								  BillingAccount account) {
		var prep = TransactionPrepare.builder()
				.txType(tx_type.name())
				.txName(tx_name)
				.txPrepLog(prep_log)
				.status("START")
				.issuedBy(account)
				.build();
		prep = transactionPrepareRepository.save(prep);

		return BillingTransactionManager.builder()
				.billingAccountRepository(billingAccountRepository)
				.transactionRepository(transactionRepository)
				.transactionPrepareRepository(transactionPrepareRepository)
				.ledgerEntryLinkRepository(ledgerEntryLinkRepository)
				.ledgerEntryRepository(ledgerEntryRepository)
				.prep(prep)
				.build();
	}
}