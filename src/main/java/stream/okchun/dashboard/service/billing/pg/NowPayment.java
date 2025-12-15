package stream.okchun.dashboard.service.billing.pg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@Service
@Primary
public class NowPayment implements PaymentGateway {
	private final RestClient httpClient = RestClient.create();
	@Value("${okchun.payment.success_url:https://okchun.esukmean.com/billing/pg/success?order_id={ORDER_ID}}")
	private String SUCCESS_URL;

	@Retryable(maxAttempts = 2, backoff = @Backoff(delay = 100))
	public InvoiceCreatedResult createInvoice(@NonNull String currency, long amount,
											  @NonNull String order_id, @NonNull String order_description,
											  @Nullable String success_url) {
		if (success_url == null) {
			success_url = SUCCESS_URL.replace("{ORDER_ID}", order_id);
		}

		var req = httpClient.post()
				.uri("https://api.nowpayments.io/v1/invoice")
				.header("x-api-key", "Z8WB86B-E61428S-P94H0ZN-1VKRQYR")
				.body(new invoiceCreation(amount, currency, order_id, order_description, success_url, true,
						true))
				.contentType(MediaType.APPLICATION_JSON)
				.retrieve()
				.body(InvoiceCreated.class);

		return new InvoiceCreatedResult(req.id(), req.orderId(), req.invoiceUrl());
	}
}

record invoiceCreation(
		long price_amount,
		String price_currency,
		String order_id,
		String order_description,
		String success_url,
		boolean is_fixed_rate,
		boolean is_fee_paid_by_user
) {}
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
record InvoiceCreated(
		@NonNull
		String id,

		@JsonProperty("token_id")
		String tokenId,

		@NonNull
		@JsonProperty("order_id")
		String orderId,

		@JsonProperty("order_description")
		String orderDescription,

		// API says String; keep as String to match payload (you can switch to BigDecimal if desired)
		@JsonProperty("price_amount")
		String priceAmount,

		@JsonProperty("price_currency")
		String priceCurrency,

		@JsonProperty("pay_currency")
		String payCurrency,

		@JsonProperty("ipn_callback_url")
		String ipnCallbackUrl,

		@NonNull
		@JsonProperty("invoice_url")
		String invoiceUrl,

		@JsonProperty("success_url")
		String successUrl,

		@JsonProperty("cancel_url")
		String cancelUrl,

		@JsonProperty("partially_paid_url")
		String partiallyPaidUrl,

		@JsonProperty("payout_currency")
		String payoutCurrency,

		@JsonProperty("created_at")
		String createdAt,

		@JsonProperty("updated_at")
		String updatedAt,

		@JsonProperty("is_fixed_rate")
		boolean isFixedRate,

		@JsonProperty("is_fee_paid_by_user")
		boolean isFeePaidByUser
) {
	/*
	{
		  "payment_id": "5745459419",
		  "payment_status": "waiting",
		  "pay_address": "3EZ2uTdVDAMFXTfc6uLDDKR6o8qKBZXVkj",
		  "price_amount": 3999.5,
		  "price_currency": "usd",
		  "pay_amount": 0.17070286,
		  "pay_currency": "btc",
		  "order_id": "RGDBP-21314",
		  "order_description": "Apple Macbook Pro 2019 x 1",
		  "ipn_callback_url": "https://nowpayments.io",
		  "created_at": "2020-12-22T15:00:22.742Z",
		  "updated_at": "2020-12-22T15:00:22.742Z",
		  "purchase_id": "5837122679",
		  "amount_received": null,
		  "payin_extra_id": null,
		  "smart_contract": "",
		  "network": "btc",
		  "network_precision": 8,
		  "time_limit": null,
		  "burning_percent": null,
		  "expiration_estimate_date": "2020-12-23T15:00:22.742Z"
	}
	 */
}

/** Nested object: redirectData.redirect_url */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
record RedirectData(
		String redirectUrl
) {}
