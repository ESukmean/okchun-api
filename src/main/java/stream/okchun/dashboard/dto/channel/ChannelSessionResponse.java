package stream.okchun.dashboard.dto.channel;

import stream.okchun.dashboard.database.entity.media.ChannelSession;
import stream.okchun.dashboard.database.entity.media.ChannelSessionType;

import java.time.OffsetDateTime;

public record ChannelSessionResponse(
		String session_id,
		ChannelSessionType state,
		OffsetDateTime started_at,
		OffsetDateTime ended_at,
		long total_bytes_up,
		long total_bytes_down,
		OffsetDateTime created_at,
		OffsetDateTime updated_at
) {
	public static ChannelSessionResponse of(ChannelSession session) {
		return ChannelSessionMapper.INSTANCE.toResponse(session);
	}
}