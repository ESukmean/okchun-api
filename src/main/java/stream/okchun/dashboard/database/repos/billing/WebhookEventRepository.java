package stream.okchun.dashboard.database.repos.billing;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.billing.WebhookEvent;

@Repository
public interface WebhookEventRepository extends CrudRepository<@NonNull WebhookEvent, @NonNull Long>,
		WebhookEventRepositoryCustom {
}
