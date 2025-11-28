package stream.okchun.dashboard.database.repos.media;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.media.Channel;

public interface ChannelRepository extends CrudRepository<@NonNull Channel, @NonNull Long>, ChannelRepositoryCustom {
}
