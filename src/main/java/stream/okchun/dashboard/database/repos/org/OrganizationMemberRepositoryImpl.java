package stream.okchun.dashboard.database.repos.org;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrganizationMemberRepositoryImpl implements OrganizationMemberRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Autowired
	public OrganizationMemberRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}
}
