package stream.okchun.dashboard.controller.billing;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/organizations/{org_id}/billing")
public class BillingController {

    // Credit & balance
    @GetMapping("/credit")
    public String getCreditBalance(@PathVariable("org_id") String orgId) {
        return "Credit balance for organization " + orgId;
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
    public String listTopupHistory(@PathVariable("org_id") String orgId, @RequestParam(required = false) String cursor) {
        return "Top-up history for organization " + orgId;
    }

    // Usage & charges log
    @GetMapping("/usage")
    public String getDetailedUsage(@PathVariable("org_id") String orgId, @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) String cursor) {
        return "Detailed usage for organization " + orgId;
    }

    @GetMapping("/summary")
    public String getAggregatedUsage(@PathVariable("org_id") String orgId, @RequestParam(required = false) String period) {
        return "Aggregated usage for organization " + orgId;
    }

    // Webhooks (internal) - This endpoint has a different base path, will be handled separately in AdminController or a dedicated webhook controller if needed.
    // POST /v1/webhooks/lemonsqueezy
}
