package stream.okchun.dashboard.service.billing.ledger;

public enum LedgerSide {
	DEBIT,
	CREDIT;

	@Override
	public String toString() {
		return switch (this) {
			case DEBIT -> "D";
			case CREDIT -> "C";
		};
	}
}
