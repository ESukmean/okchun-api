package stream.okchun.dashboard.database.repos.media;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelSessionRepositoryImpl implements ChannelSessionRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Autowired
	public ChannelSessionRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}
}
