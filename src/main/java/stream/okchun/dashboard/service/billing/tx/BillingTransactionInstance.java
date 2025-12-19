package stream.okchun.dashboard.service.billing.tx;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import stream.okchun.dashboard.database.entity.billing.Transaction;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.repos.billing.*;
import stream.okchun.dashboard.exception.billing.TransactionException;
import stream.okchun.dashboard.service.CurrencyExchangeService;
import stream.okchun.dashboard.service.billing.ledger.LedgerSide;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class BillingTransactionInstance {
	private final BillingAccountRepository billingAccountRepository;
	private final TransactionRepository transactionRepository;
	private final TransactionPrepareRepository transactionPrepareRepository;
	private final LedgerEntryLinkRepository ledgerEntryLinkRepository;
	private final LedgerEntryRepository ledgerEntryRepository;
	private final CurrencyExchangeService exchangeService;
	@Getter
	private final TransactionPrepare prep;
	private Transaction transaction;
	// 사실 SUM 타입 하나만 있으면 진짜 깔끔하게 tree가 그려지는데... SUM 타입이 없어서 그게 안됨...
	@Getter
	private final BillingTransactionLedger creditLedgerEntry;
	@Getter
	private final BillingTransactionLedger debitLedgerEntry;
	@Getter
	@Setter
	private String mainCurrency;
	@Setter
	private Transaction relatedTransaction;

	@Builder
	public BillingTransactionInstance(BillingAccountRepository billingAccountRepository,
									  TransactionRepository transactionRepository,
									  TransactionPrepareRepository transactionPrepareRepository,
									  LedgerEntryLinkRepository ledgerEntryLinkRepository,
									  LedgerEntryRepository ledgerEntryRepository,
									  CurrencyExchangeService exchangeService, TransactionPrepare prep) {
		this.billingAccountRepository = billingAccountRepository;
		this.transactionRepository = transactionRepository;
		this.transactionPrepareRepository = transactionPrepareRepository;
		this.ledgerEntryLinkRepository = ledgerEntryLinkRepository;
		this.ledgerEntryRepository = ledgerEntryRepository;
		this.exchangeService = exchangeService;
		this.prep = prep;

		this.creditLedgerEntry = new BillingTransactionLedger(prep, null, LedgerSide.CREDIT);
		this.debitLedgerEntry = new BillingTransactionLedger(prep, null, LedgerSide.DEBIT);
	}

	@Transactional
	public boolean commit() throws TransactionException {
		var tx = this.getTransaction();
		this.prep.setStatus("TX_START");
		try {
			// 일부러 Transaction 테이블은 수정 불가함 (DB에서 table update 권한 빼놓음)
			// 그래서 저장시에 오류가 발생할 수 있음. 오류 발생시 tx 실패로 처리
			transaction = this.transactionRepository.save(tx);
		} catch (Exception e) {
			throw TransactionException.DB_COMMIT_FAILED(e.getMessage() + "\r\n\r\n" + Arrays.stream(
							e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining()),
					tx.getId(), debitLedgerEntry, creditLedgerEntry);
		}

		debitLedgerEntry.commitDB(tx, ledgerEntryLinkRepository, ledgerEntryRepository,
				billingAccountRepository);
		creditLedgerEntry.commitDB(tx, ledgerEntryLinkRepository, ledgerEntryRepository,
				billingAccountRepository);


		this.prep.setStatus("OK");
		return true;
	}

	private Transaction getTransaction() throws TransactionException {
		if (transaction != null) {
			throw TransactionException.ALREADY_COMMITED("", getTxId(), debitLedgerEntry, creditLedgerEntry);
		}

		var amountCredit = creditLedgerEntry.calculateSum(new HashMap<>(2));
		var amountDebit = debitLedgerEntry.calculateSum(new HashMap<>(2));

		if (!amountCredit.equals(amountDebit)) {
			throw TransactionException.CREDIT_DEBIT_AMOUNT_MISMATCH(
					"credit = " + amountCredit + " / debit = " + amountDebit, prep.getId(), debitLedgerEntry,
					creditLedgerEntry);
		}

		if (mainCurrency == null) {
			var maxBalance = BigDecimal.ZERO;
			var maxCurrency = "";

			for (Map.Entry<String, BigDecimal> amount : amountCredit.entrySet()) {
				if (amount.getValue().compareTo(maxBalance) > 0) {
					maxBalance = amount.getValue();
					maxCurrency = amount.getKey();
				}
			}

			mainCurrency = maxCurrency;
		}
		var amountPreview = exchangeService.exchange(mainCurrency, amountCredit);
		this.transaction = Transaction.of(prep, this.mainCurrency, amountPreview, this.relatedTransaction,
				null);


		return this.transaction;
	}

	public long getTxId() {
		return this.prep.getId();
	}

	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void appendLog(String log) {
		String existed_log = prep.getTxComment();
		if (existed_log == null) {
			existed_log = log;
		} else {
			existed_log = existed_log + "\r\n" + log;
		}

		this.prep.setTxComment(existed_log);
		transactionPrepareRepository.save(this.prep);
	}

	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void setStatus(String status) {
		this.prep.setStatus(status);
		transactionPrepareRepository.save(this.prep);
	}
}
