package stream.okchun.dashboard.exception.billing;

import lombok.Getter;
import stream.okchun.dashboard.service.billing.ledger.LedgerTreeEntry;

import javax.annotation.Nullable;

public class TransactionException extends RuntimeException {
	private final @Nullable Long txId;
	@Getter
	private final String log;
	@Getter
	private final LedgerTreeEntry credit;
	@Getter
	private final LedgerTreeEntry debit;

	public TransactionException(String message, @Nullable Long txId, String log, LedgerTreeEntry debit,
								LedgerTreeEntry credit) {
		this.txId = txId;
		this.log = log;
		this.credit = credit;
		this.debit = debit;

		super(message);
	}

	public static TransactionException CREDIT_DEBIT_AMOUNT_MISMATCH(String log,
																	@Nullable Long txId,
																	LedgerTreeEntry debit,
																	LedgerTreeEntry credit) {
		return new TransactionException("CREDIT_DEBIT_AMOUNT_MISMATCH", txId, log, debit, credit);
	}
	public static TransactionException NO_MAIN_CURRENCY_EXIST(String log,
																	@Nullable Long txId,
																	LedgerTreeEntry debit,
																	LedgerTreeEntry credit) {
		return new TransactionException("NO_MAIN_CURRENCY_EXIST", txId, log, debit, credit);
	}
	public static TransactionException DB_COMMIT_FAILED(String log, @Nullable Long txId,
														LedgerTreeEntry debit,
														LedgerTreeEntry credit) {
		return new TransactionException("DB_COMMIT_FAILED", txId, log, debit, credit);
	}
}
