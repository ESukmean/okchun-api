package stream.okchun.dashboard.service.billing.pg;

public record InvoiceCreatedResult(
		String payment_id,
		String order_id,
		String url
) {}
