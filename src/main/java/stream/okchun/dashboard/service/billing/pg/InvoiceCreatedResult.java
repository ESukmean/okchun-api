package stream.okchun.dashboard.service.billing.pg;

public record InvoiceCreatedResult(
		boolean success,
		String payment_name,
		String payment_id,
		String order_id,
		String url,
		String log_data
) {
}
