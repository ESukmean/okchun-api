package stream.okchun.dashboard.service.billing.ledger;

import java.math.BigDecimal;

public interface LedgerEntryInterface {
	BigDecimal getAmount();
	String getCurrency();
	String getSide();
}
