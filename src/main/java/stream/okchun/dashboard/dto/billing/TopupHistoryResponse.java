package stream.okchun.dashboard.dto.billing;

import stream.okchun.dashboard.database.entity.billing.BillingInvoice;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TopupHistoryResponse(
		Long id,
		BigDecimal amount,
		String currencyCode,
		String status,
		String provider,
		OffsetDateTime createdAt
) {
	public static TopupHistoryResponse from(BillingInvoice invoice) {
		return new TopupHistoryResponse(
				invoice.getId(),
				invoice.getTotalPayAmount(),
				invoice.getCurrency(),
				invoice.getStatus(),
				invoice.getPgRef(),
				invoice.getCreatedAt()
		);
	}
}
