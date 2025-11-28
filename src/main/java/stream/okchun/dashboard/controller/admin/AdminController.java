package stream.okchun.dashboard.controller.admin;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    // Internal-only endpoints
    @GetMapping("/health")
    public String getSystemHealth() {
        return "System health status";
    }

    @GetMapping("/metrics")
    public String getSystemMetrics() {
        return "System metrics";
    }

    @GetMapping("/pools")
    public String getAllPools(@RequestParam(required = false) String region, @RequestParam(required = false) String cursor) {
        return "All pools with status";
    }

    @GetMapping("/pools/{pool_id}/status")
    public String getPoolStatus(@PathVariable("pool_id") String poolId) {
        return "Pool " + poolId + " status";
    }

    @GetMapping("/organizations/{org_id}/billing/debug")
    public String debugOrganizationBilling(@PathVariable("org_id") String orgId) {
        return "Billing debug info for organization " + orgId;
    }

    // Lemon Squeezy Webhook - Placed here as it's an internal-only endpoint
    @PostMapping("/webhooks/lemonsqueezy")
    public String handleLemonSqueezyWebhook(@RequestBody Object body) {
        return "Lemon Squeezy webhook received";
    }
}
