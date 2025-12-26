package stream.okchun.dashboard.controller.pool;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stream.okchun.dashboard.database.entity.auth.ApiKey;
import stream.okchun.dashboard.database.entity.infra.Pool;
import stream.okchun.dashboard.database.entity.org.Organization;
import stream.okchun.dashboard.dto.GlobalResponse;
import stream.okchun.dashboard.service.ApiKeyService;
import stream.okchun.dashboard.service.PoolService;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PoolController {
	private final ApiKeyService apiKeyService;
	private final PoolService poolService;

	// Pools (admin/owner operations)
	@PostMapping("/pools")
	public String createPool(@RequestBody Object body) {
		return "Pool created";
	}

	@GetMapping("/pools")
	public List<Pool> listPools(@RequestParam(required = false) Long owner_org_id,
											   @RequestParam(required = false) String region,
											   @RequestParam(required = false) Long cursor) {
		ApiKey apiKey = null;
		try {
			apiKey = apiKeyService.getHttpRequestApiKey();
		} catch (Exception e) {}

		Organization orgId = null;
		if (apiKey != null) {
			orgId = apiKey.getOrg();
		}

		return poolService.listPools(orgId, owner_org_id, region, cursor);
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
