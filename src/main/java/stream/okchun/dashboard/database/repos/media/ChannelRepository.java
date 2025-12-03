package stream.okchun.dashboard.database.repos.media;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.media.Channel;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;

public interface ChannelRepository extends CrudRepository<@NonNull Channel, @NonNull Long>, ChannelRepositoryCustom {

}
