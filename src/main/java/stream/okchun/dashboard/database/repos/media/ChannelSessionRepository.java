package stream.okchun.dashboard.database.repos.media;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import stream.okchun.dashboard.database.entity.media.ChannelSession;

public interface ChannelSessionRepository extends CrudRepository<@NonNull ChannelSession, @NonNull Long>, ChannelSessionRepositoryCustom {
}
