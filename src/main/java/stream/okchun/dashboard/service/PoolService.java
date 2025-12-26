package stream.okchun.dashboard.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.database.entity.infra.Pool;
import stream.okchun.dashboard.database.entity.org.Organization;
import stream.okchun.dashboard.database.repos.infra.PoolRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoolService {
	private final PoolRepository poolRepository;

	// Pools (admin/owner operations)
	public Object createPool(Object body) {
		return "Pool created";
	}

	public List<Pool> listPools(@Nullable Organization org, @Nullable Long ownerOrgId, @Nullable String region,
								@Nullable Long cursor) {
		return poolRepository.listAllPoolByCondition(org == null ? null : org.getId(), ownerOrgId, region,
				cursor);
	}

	public Object getPoolDetail(String poolId) {
		return "Pool detail for " + poolId;
	}

	public Object updatePool(String poolId, Object body) {
		return "Pool " + poolId + " updated";
	}

	public Object deletePool(String poolId) {
		return "Pool " + poolId + " deactivated";
	}

	// Org <-> Pool linking
	public Object listOrgPools(String orgId, String type, String region, String cursor) {
		return "List of pools for organization " + orgId;
	}

	public Object attachPool(String orgId, String poolId) {
		return "Pool " + poolId + " attached to organization " + orgId;
	}

	public Object detachPool(String orgId, String poolId) {
		return "Pool " + poolId + " detached from organization " + orgId;
	}

	// Pool reporting (Rust server)
	public Object poolReport(Object body) {
		return "Pool report received and processed";
	}
}
