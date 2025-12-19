package stream.okchun.dashboard.service.billing.tx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import stream.okchun.dashboard.database.entity.billing.BillingAccount;
import stream.okchun.dashboard.database.entity.billing.LedgerEntry;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.service.billing.ledger.LedgerSide;
import stream.okchun.dashboard.service.billing.ledger.LedgerTreeType;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BillingTransactionLedgerTest {

	private TransactionPrepare mockPrep;
	private BillingAccount mockAccount1;
	private BillingAccount mockAccount2;

	@BeforeEach
	void setUp() {
		mockPrep = TransactionPrepare.builder().id(1L).txName("Test TX").build();
		mockAccount1 = BillingAccount.builder().id(10L).currency("USD").build();
		mockAccount2 = BillingAccount.builder().id(20L).currency("KRW").build();
	}

	@Test
	@DisplayName("단일 통화 합계 계산 테스트")
	void testCalculateSum_SingleCurrency() {
		BillingTransactionLedger root = new BillingTransactionLedger(mockPrep, null, LedgerSide.CREDIT);
		root.addLedgerEntry(LedgerEntry.builder().account(mockAccount1).amount(BigDecimal.valueOf(100)).side("C").build());
		root.addLedgerEntry(LedgerEntry.builder().account(mockAccount1).amount(BigDecimal.valueOf(50)).side("C").build());

		Map<String, BigDecimal> sum = root.calculateSum(new HashMap<>());
		assertEquals(1, sum.size());
		assertEquals(0, BigDecimal.valueOf(150).compareTo(sum.get("USD")));
	}

	@Test
	@DisplayName("다중 통화 합계 계산 테스트")
	void testCalculateSum_MultiCurrency() {
		BillingTransactionLedger root = new BillingTransactionLedger(mockPrep, null, LedgerSide.DEBIT);
		root.addLedgerEntry(LedgerEntry.builder().account(mockAccount1).amount(BigDecimal.valueOf(100)).side("D").build());
		root.addLedgerEntry(LedgerEntry.builder().account(mockAccount2).amount(BigDecimal.valueOf(5000)).side("D").build());
		root.addLedgerEntry(LedgerEntry.builder().account(mockAccount1).amount(BigDecimal.valueOf(25)).side("D").build());

		Map<String, BigDecimal> sum = root.calculateSum(new HashMap<>());
		assertEquals(2, sum.size());
		assertEquals(0, BigDecimal.valueOf(125).compareTo(sum.get("USD")));
		assertEquals(0, BigDecimal.valueOf(5000).compareTo(sum.get("KRW")));
	}

	@Test
	@DisplayName("하위 트리를 포함한 합계 계산 테스트")
	void testCalculateSum_WithSubTree() {
		BillingTransactionLedger root = new BillingTransactionLedger(mockPrep, null, LedgerSide.CREDIT);
		root.addLedgerEntry(LedgerEntry.builder().account(mockAccount1).amount(BigDecimal.valueOf(100)).side("C").build());

		BillingTransactionLedger subTree = root.createSubTree(LedgerTreeType.ROOT, "Sub-tree");
		subTree.addLedgerEntry(LedgerEntry.builder().account(mockAccount2).amount(BigDecimal.valueOf(3000)).side("C").build());
		subTree.addLedgerEntry(LedgerEntry.builder().account(mockAccount1).amount(BigDecimal.valueOf(75)).side("C").build());


		BillingTransactionLedger subSubTree = subTree.createSubTree(LedgerTreeType.ROOT, "Fee sub-tree");
		subSubTree.addLedgerEntry(LedgerEntry.builder().account(mockAccount2).amount(BigDecimal.valueOf(1000)).side("C").build());

		Map<String, BigDecimal> sum = root.calculateSum(new HashMap<>());
		assertEquals(2, sum.size());
		assertEquals(0, BigDecimal.valueOf(175).compareTo(sum.get("USD")));
		assertEquals(0, BigDecimal.valueOf(4000).compareTo(sum.get("KRW")));
	}
}
