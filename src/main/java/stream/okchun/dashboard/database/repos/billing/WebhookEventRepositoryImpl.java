package stream.okchun.dashboard.database.repos.billing;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WebhookEventRepositoryImpl implements WebhookEventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public WebhookEventRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
