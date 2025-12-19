package stream.okchun.dashboard.database.repos.org;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrganizationPoolRepositoryImpl implements OrganizationPoolRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Autowired
	public OrganizationPoolRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}
}
