package stream.okchun.dashboard.database.repos.billing;

import jakarta.annotation.Nullable;
import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.billing.BillingAccount;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingAccountRepository extends CrudRepository<@NonNull BillingAccount, @NonNull Long>,
		BillingAccountRepositoryCustom {
	List<BillingAccount> findAllByAccountTypeAndAccountRef(String AccountType, long AccountRef);

	Optional<BillingAccount> findByAccountTypeAndAccountRefAndCurrency(String AccountType, long AccountRef,
																	   String currency);


	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select a from BillingAccount a where a.id = :id")
	@Nullable
	BillingAccount findByIdForUpdate(@Param("id") Long id);
}
