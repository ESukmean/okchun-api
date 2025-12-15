package stream.okchun.dashboard.service.billing.pg;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NowPaymentTest {
	@Autowired
	@Setter
	private NowPayment pg;

	@Value("${okchun.payment.success_url:https://okchun.esukmean.com/billing/pg/success}")
	private String SUCCESS_URL;

	@Test
	void createInvoice() {
		var result = pg.createInvoice("USD", 100, "esm-test", "API integrate test", SUCCESS_URL);
		System.out.println(result.order_id());
		System.out.println(result.payment_id());
		System.out.println(result.url());
	}
}