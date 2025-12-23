package stream.okchun.dashboard.dto.billing;

import java.math.BigDecimal;

public record CreditBalanceResponse(
		BigDecimal balance,
		String currencyCode
) {
	public static CreditBalanceResponse of(
			BigDecimal balance,
			String currencyCode
	) {
		return new CreditBalanceResponse(balance, currencyCode);
	}
}
