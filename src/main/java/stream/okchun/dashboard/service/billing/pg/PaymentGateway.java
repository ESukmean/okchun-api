package stream.okchun.dashboard.service.billing.pg;

import jakarta.annotation.Nullable;

public interface PaymentGateway {
	InvoiceCreatedResult createInvoice(String currency, long amount, String order_id,
									   String order_description, @Nullable String success_url);

	int getPGNumber();
}

