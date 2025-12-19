package stream.okchun.dashboard.dto.channel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import stream.okchun.dashboard.database.entity.media.Channel;
import stream.okchun.dashboard.database.entity.media.ChannelSession;

@Mapper
public interface ChannelMapper {

	ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);

	@Named("channelSessionResponseOf")
	static ChannelSessionResponse channelSessionResponseOf(ChannelSession session) {
		return ChannelSessionResponse.of(session);
	}

	@Mapping(source = "id", target = "session_id")
	@Mapping(source = "latest_session", target = "session", qualifiedByName = "channelSessionResponseOf")
	ChannelResponse toResponse(Channel channel);
}
