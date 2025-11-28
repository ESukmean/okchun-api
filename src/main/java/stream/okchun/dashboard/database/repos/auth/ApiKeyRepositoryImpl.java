package stream.okchun.dashboard.database.repos.auth;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ApiKeyRepositoryImpl implements ApiKeyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public ApiKeyRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
