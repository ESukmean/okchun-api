package stream.okchun.dashboard.controller.pool;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class PoolController {

	// Pools (admin/owner operations)
	@PostMapping("/pools")
	public String createPool(@RequestBody Object body) {
		return "Pool created";
	}

	@GetMapping("/pools")
	public String listPools(@RequestParam(required = false) String owner_org_id,
							@RequestParam(required = false) String visibility,
							@RequestParam(required = false) String region,
							@RequestParam(required = false) String cursor) {
		return "List of pools";
	}

	@GetMapping("/pools/{pool_id}")
	public String getPoolDetail(@PathVariable("pool_id") String poolId) {
		return "Pool detail for " + poolId;
	}

	@PatchMapping("/pools/{pool_id}")
	public String updatePool(@PathVariable("pool_id") String poolId, @RequestBody Object body) {
		return "Pool " + poolId + " updated";
	}

	@DeleteMapping("/pools/{pool_id}")
	public String deletePool(@PathVariable("pool_id") String poolId) {
		return "Pool " + poolId + " deactivated";
	}

	// Org <-> Pool linking
	@GetMapping("/organizations/{org_id}/pools")
	public String listOrgPools(@PathVariable("org_id") String orgId,
							   @RequestParam(required = false) String type,
							   @RequestParam(required = false) String region,
							   @RequestParam(required = false) String cursor) {
		return "List of pools for organization " + orgId;
	}

	@PostMapping("/organizations/{org_id}/pools/{pool_id}:attach")
	public String attachPool(@PathVariable("org_id") String orgId, @PathVariable("pool_id") String poolId) {
		return "Pool " + poolId + " attached to organization " + orgId;
	}

	@DeleteMapping("/organizations/{org_id}/pools/{pool_id}:detach")
	public String detachPool(@PathVariable("org_id") String orgId, @PathVariable("pool_id") String poolId) {
		return "Pool " + poolId + " detached from organization " + orgId;
	}

	// Pool reporting (Rust server)
	@PostMapping("/pool/report")
	public String poolReport(@RequestBody Object body) {
		return "Pool report received";
	}
}
