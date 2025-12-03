package stream.okchun.dashboard.database.repos.media;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import stream.okchun.dashboard.database.entity.media.Channel;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;

import java.util.List;

public interface ChannelRepositoryCustom {
	List<Channel> searchAllByOrg(@NonNull Long org_id, @Nullable String name, @Nullable ChannelStateType state,
								 @Nullable Integer cursor);
}
