package stream.okchun.dashboard.dto.billing;

import java.math.BigDecimal;

public record CreditBalanceResponse(
		BigDecimal balance,
		String currencyCode,
		BigDecimal lowBalanceNotificationThreshold,
		String estimatedRemaining
) {
}
