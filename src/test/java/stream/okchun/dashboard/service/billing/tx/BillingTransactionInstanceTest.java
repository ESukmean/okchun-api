package stream.okchun.dashboard.service.billing.tx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import stream.okchun.dashboard.database.entity.billing.BillingAccount;
import stream.okchun.dashboard.database.entity.billing.LedgerEntry;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.repos.billing.*;
import stream.okchun.dashboard.exception.billing.TransactionException;
import stream.okchun.dashboard.service.CurrencyExchangeService;
import stream.okchun.dashboard.service.billing.ledger.LedgerSide;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingTransactionInstanceTest {

	@Mock
	private BillingAccountRepository billingAccountRepository;
	@Mock
	private TransactionRepository transactionRepository;
	@Mock
	private TransactionPrepareRepository transactionPrepareRepository;
	@Mock
	private LedgerEntryLinkRepository ledgerEntryLinkRepository;
	@Mock
	private LedgerEntryRepository ledgerEntryRepository;
	@Mock
	private CurrencyExchangeService exchangeService;

	private BillingTransactionInstance txInstance;
	private TransactionPrepare prep;
	private BillingAccount accountA;
	private BillingAccount accountB;

	@BeforeEach
	void setUp() {
		prep = TransactionPrepare.builder().id(1L).txName("Test Transaction").build();
		accountA = BillingAccount.builder().id(101L).currency("USD").balance(BigDecimal.ZERO).build();
		accountB = BillingAccount.builder().id(102L).currency("USD").balance(BigDecimal.valueOf(1000)).build();

		// BillingTransactionInstance needs to be built with all mocks
		txInstance = BillingTransactionInstance.builder()
				.billingAccountRepository(billingAccountRepository)
				.transactionRepository(transactionRepository)
				.transactionPrepareRepository(transactionPrepareRepository)
				.ledgerEntryLinkRepository(ledgerEntryLinkRepository)
				.ledgerEntryRepository(ledgerEntryRepository)
				.exchangeService(exchangeService)
				.prep(prep)
				.build();
	}

	@Test
	@DisplayName("차변/대변 금액 불일치 시 예외 발생")
	void testCommit_throwsExceptionOnMismatch() {
		// 대변 (Credit)
		txInstance.getCreditLedgerEntry().addLedgerEntry(
				LedgerEntry.builder().account(accountA).amount(BigDecimal.valueOf(100)).side("C").build()
		);
		// 차변 (Debit) - 금액 불일치
		txInstance.getDebitLedgerEntry().addLedgerEntry(
				LedgerEntry.builder().account(accountB).amount(BigDecimal.valueOf(99)).side("D").build()
		);

		assertThrows(TransactionException.class, () -> {
			txInstance.commit();
		}, "차변과 대변의 합계가 일치하지 않을 때 TransactionException이 발생해야 합니다.");
	}

	@Test
	@DisplayName("정상적인 트랜잭션 커밋 성공")
	void testCommit_success() {
		// 대변 (Credit)
		txInstance.getCreditLedgerEntry().addLedgerEntry(
				LedgerEntry.builder().account(accountA).amount(BigDecimal.valueOf(100)).side("C").build()
		);
		// 차변 (Debit)
		txInstance.getDebitLedgerEntry().addLedgerEntry(
				LedgerEntry.builder().account(accountB).amount(BigDecimal.valueOf(100)).side("D").build()
		);

		// Mocking behavior for a successful commit
		when(exchangeService.exchange(anyString(), any(HashMap.class))).thenReturn(BigDecimal.ZERO);
		when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(ledgerEntryLinkRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// We need to mock the commitDB chain
		doNothing().when(billingAccountRepository).save(any());
		when(ledgerEntryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(billingAccountRepository.findByIdForUpdate(any())).thenReturn(accountA, accountB);


		assertDoesNotThrow(() -> {
			txInstance.commit();
		}, "정상적인 트랜잭션 커밋 시 예외가 발생하지 않아야 합니다.");
	}
}
