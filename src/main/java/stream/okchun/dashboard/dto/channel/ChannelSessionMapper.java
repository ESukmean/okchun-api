package stream.okchun.dashboard.dto.channel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import stream.okchun.dashboard.database.entity.media.ChannelSession;

@Mapper
public interface ChannelSessionMapper {

	ChannelSessionMapper INSTANCE = Mappers.getMapper(ChannelSessionMapper.class);

	@Mapping(source = "externalSessionId", target = "session_id")
	@Mapping(source = "startedAt", target = "started_at")
	@Mapping(source = "endedAt", target = "ended_at")
	@Mapping(source = "totalBytesUp", target = "total_bytes_up")
	@Mapping(source = "totalBytesDown", target = "total_bytes_down")
	@Mapping(source = "createdAt", target = "created_at")
	@Mapping(source = "updatedAt", target = "updated_at")
	ChannelSessionResponse toResponse(ChannelSession session);
}
