package stream.okchun.dashboard.controller.usage;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/organizations/{org_id}/usage")
public class UsageController {

	// Usage / analytics
	@GetMapping("/summary")
	public String getUsageSummary(@PathVariable("org_id") String orgId,
								  @RequestParam(required = false) String from,
								  @RequestParam(required = false) String to) {
		return "Usage summary for organization " + orgId;
	}

	@GetMapping("/channels")
	public String getChannelUsage(@PathVariable("org_id") String orgId,
								  @RequestParam(required = false) String from,
								  @RequestParam(required = false) String to,
								  @RequestParam(required = false) String cursor) {
		return "Channel usage for organization " + orgId;
	}

	@GetMapping("/pools")
	public String getPoolUsage(@PathVariable("org_id") String orgId,
							   @RequestParam(required = false) String from,
							   @RequestParam(required = false) String to,
							   @RequestParam(required = false) String cursor) {
		return "Pool usage for organization " + orgId;
	}

	// Audit logs
	@GetMapping("/audit-logs")
	public String getAuditLogs(@PathVariable("org_id") String orgId,
							   @RequestParam(required = false) String actor,
							   @RequestParam(required = false) String action,
							   @RequestParam(required = false) String from,
							   @RequestParam(required = false) String to,
							   @RequestParam(required = false) String cursor) {
		return "Audit logs for organization " + orgId;
	}
}
