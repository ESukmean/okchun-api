package stream.okchun.dashboard.database.repos.infra;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.infra.Pool;
import stream.okchun.dashboard.database.entity.infra.PoolVisibility;
import stream.okchun.dashboard.database.entity.infra.QPool;
import stream.okchun.dashboard.database.entity.org.QOrganizationPool;

import java.util.List;

@Repository
public class PoolRepositoryImpl implements PoolRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Autowired
	public PoolRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	public List<Pool> listAllPoolByCondition(@Nullable Long orgId, @Nullable Long ownerOrgId,
											 @Nullable String region, @Nullable Long cursor) {
		QPool pool = QPool.pool;
		QOrganizationPool orgPool = QOrganizationPool.organizationPool;


		var visibleConditionBuilder = new BooleanBuilder();
		if (orgId != null) {
			visibleConditionBuilder.or(pool.id.in(JPAExpressions.select(orgPool.id.poolId)
					.from(orgPool)
					.where(orgPool.id.orgId.eq(orgId))).and(pool.visibility.eq(PoolVisibility.PRIVATE)));
		}

		visibleConditionBuilder.or(pool.visibility.in(PoolVisibility.SHARED, PoolVisibility.PUBLIC_MARKET));


		return queryFactory.selectFrom(pool).where(visibleConditionBuilder,
				ownerOrgId != null ? pool.ownerOrg.id.eq(ownerOrgId) : null,
				region != null ? pool.regionCode.eq(region) : null,
				cursor != null ? pool.id.gt(cursor) : null, pool.deletedAt.isNotNull()).limit(50).orderBy(
				pool.id.asc()).fetch();
	}
}
