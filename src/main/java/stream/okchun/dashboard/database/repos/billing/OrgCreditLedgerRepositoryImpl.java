package stream.okchun.dashboard.database.repos.billing;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrgCreditLedgerRepositoryImpl implements OrgCreditLedgerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public OrgCreditLedgerRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
