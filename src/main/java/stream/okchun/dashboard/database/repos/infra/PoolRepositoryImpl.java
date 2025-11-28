package stream.okchun.dashboard.database.repos.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PoolRepositoryImpl implements PoolRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public PoolRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
