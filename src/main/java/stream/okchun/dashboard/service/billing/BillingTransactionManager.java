package stream.okchun.dashboard.service.billing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.repos.billing.*;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BillingTransactionManager {
	private final BillingAccountRepository billingAccountRepository;
	private final TransactionRepository transactionRepository;
	private final TransactionPrepareRepository transactionPrepareRepository;
	private final LedgerEntryLinkRepository ledgerEntryLinkRepository;
	private final LedgerEntryRepository ledgerEntryRepository;

	private final TransactionPrepare prep;

	// 사실 SUM 타입 하나만 있으면 진짜 깔끔하게 tree가 그려지는데... SUM 타입이 없어서 그게 안됨...
	private List<LedgerTreeEntry> creditLedgerEntry;
	private List<LedgerTreeEntry> creditLedgerEntry;



	public long getTransactionId() {
		return prep.getId();
	}


}
