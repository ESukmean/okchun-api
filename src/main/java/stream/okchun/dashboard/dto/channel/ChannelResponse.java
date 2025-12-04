package stream.okchun.dashboard.dto.channel;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;

import stream.okchun.dashboard.database.entity.media.Channel;

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
