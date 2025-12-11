package stream.okchun.dashboard.database.repos.billing;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionPrepareRepositoryImpl implements TransactionPrepareRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public TransactionPrepareRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
