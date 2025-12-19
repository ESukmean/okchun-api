package stream.okchun.dashboard.dto.channel;

import stream.okchun.dashboard.database.entity.media.Channel;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;

import java.time.OffsetDateTime;
import java.util.List;

public record ChannelResponse(
		String session_id,
		String name,
		String description,
		List<String> tags,

		ChannelStateType state,
		OffsetDateTime archivedAt,

		OffsetDateTime createdAt,
		OffsetDateTime updatedAt,

		ChannelSessionResponse session
) {
	public static ChannelResponse of(Channel channel) {
		return ChannelMapper.INSTANCE.toResponse(channel);
	}
}
