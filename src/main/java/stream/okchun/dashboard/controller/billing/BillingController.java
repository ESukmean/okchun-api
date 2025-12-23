package stream.okchun.dashboard.controller.billing;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import stream.okchun.dashboard.application.OrganizationApplication;
import stream.okchun.dashboard.dto.GlobalResponse;
import stream.okchun.dashboard.dto.billing.CreditBalanceResponse;
import stream.okchun.dashboard.dto.billing.TopupHistoryResponse;
import stream.okchun.dashboard.service.AccountService;
import stream.okchun.dashboard.service.ApiKeyService;

import java.util.List;

@RestController
@RequestMapping("/v1/organizations/{org_id}/billing")
@RequiredArgsConstructor
public class BillingController {
	private final OrganizationApplication orgApplication;
	private final AccountService accountService;
	private final ApiKeyService apiKeyService;

	// Credit & balance
	@GetMapping("/credit")
	public GlobalResponse<List<CreditBalanceResponse>> getCreditBalance(
			@PathVariable("org_id") String orgId) {
		var key = apiKeyService.getHttpRequestApiKey();
		var balances = orgApplication.getBalance(key);

		return GlobalResponse.success(balances);
	}

	@PatchMapping("/credit/settings")
	public String updateCreditSettings(@PathVariable("org_id") String orgId, @RequestBody Object body) {
		return "Credit settings updated for organization " + orgId;
	}

	// Credit top-up
	@PostMapping("/credit/topups")
	public String createTopupIntent(@PathVariable("org_id") String orgId, @RequestBody Object body) {
		return "Top-up intent created for organization " + orgId;
	}

	@GetMapping("/credit/topups")
	public GlobalResponse<Page<TopupHistoryResponse>> listTopupHistory(@PathVariable("org_id") String orgId,
																	   @RequestParam(required = false) Integer page) {
		var key = apiKeyService.getHttpRequestApiKey();

		return GlobalResponse.success(orgApplication.listTopUp(key, page));
	}

	// Usage & charges log
	@GetMapping("/usage")
	public String getDetailedUsage(@PathVariable("org_id") String orgId,
								   @RequestParam(required = false) String from,
								   @RequestParam(required = false) String to,
								   @RequestParam(required = false) String cursor) {
		return "Detailed usage for organization " + orgId;
	}

	@GetMapping("/summary")
	public String getAggregatedUsage(@PathVariable("org_id") String orgId,
									 @RequestParam(required = false) String period) {
		return "Aggregated usage for organization " + orgId;
	}

	// Webhooks (internal) - This endpoint has a different base path, will be handled separately in AdminController or a dedicated webhook controller if needed.
	// POST /v1/webhooks/lemonsqueezy
}
