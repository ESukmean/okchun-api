package stream.okchun.dashboard.database.repos.media;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import stream.okchun.dashboard.database.entity.media.Channel;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;

import java.util.List;

public interface ChannelRepositoryCustom {
	@EntityGraph(attributePaths = {"latest_session"})
	List<Channel> searchAllByOrg(@NonNull Long org_id, @Nullable String name,
								 @Nullable ChannelStateType state,
								 @Nullable Integer cursor);
}
