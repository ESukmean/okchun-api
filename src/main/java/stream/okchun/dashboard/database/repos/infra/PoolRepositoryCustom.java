package stream.okchun.dashboard.database.repos.infra;

import jakarta.annotation.Nullable;
import stream.okchun.dashboard.database.entity.infra.Pool;

import java.util.List;

public interface PoolRepositoryCustom {
	List<Pool> listAllPoolByCondition(@Nullable Long orgId, @Nullable Long ownerUserId, @Nullable String region,
									  @Nullable Long cursor);
}
