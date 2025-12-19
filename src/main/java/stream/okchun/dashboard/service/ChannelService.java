package stream.okchun.dashboard.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.database.entity.media.Channel;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;
import stream.okchun.dashboard.database.repos.media.ChannelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {
	private final ChannelRepository channelRepository;

	// Channels
	public Object createChannel(String orgId, Object body) {
		return "Channel created for organization " + orgId;
	}

	/// @param state : null, on, off
	public List<Channel> listChannels(long orgFk, @Nullable String search, @Nullable ChannelStateType state,
									  @Nullable Integer cursor) {
		return channelRepository.searchAllByOrg(orgFk, search, state, cursor);
	}

	public Object getChannelDetail(String orgId, String channelId) {
		return "Channel detail for " + channelId + " in organization " + orgId;
	}

	public Object updateChannel(String orgId, String channelId, Object body) {
		return "Channel " + channelId + " updated in organization " + orgId;
	}

	public Object archiveChannel(String orgId, String channelId) {
		return "Channel " + channelId + " archived in organization " + orgId;
	}

	// Channel settings and extensions
	public Object getChannelSettings(String channelId) {
		return "Settings for channel " + channelId;
	}

	public Object updateChannelSettings(String channelId, Object body) {
		return "Settings updated for channel " + channelId;
	}

	// Extensions
	public Object listExtensions(String channelId) {
		return "Extensions for channel " + channelId;
	}

	public Object createExtension(String channelId, Object body) {
		return "Extension created for channel " + channelId;
	}

	public Object getExtensionDetail(String channelId, String extensionId) {
		return "Extension " + extensionId + " detail for channel " + channelId;
	}

	public Object updateExtension(String channelId, String extensionId, Object body) {
		return "Extension " + extensionId + " updated for channel " + channelId;
	}

	public Object removeExtension(String channelId, String extensionId) {
		return "Extension " + extensionId + " removed from channel " + channelId;
	}

	public Object invokeExtension(String channelId, String extensionId, Object body) {
		return "Extension " + extensionId + " invoked for channel " + channelId;
	}

	// Sessions (per broadcast)
	public Object createSession(String channelId, Object body) {
		return "Session created for channel " + channelId;
	}

	public Object listSessions(String channelId, String state, String cursor) {
		return "List of sessions for channel " + channelId;
	}

	public Object getSessionDetail(String channelId, String sessionId) {
		return "Session " + sessionId + " detail for channel " + channelId;
	}

	public Object stopSession(String channelId, String sessionId) {
		return "Session " + sessionId + " stopped for channel " + channelId;
	}

	public Object getSessionStats(String channelId, String sessionId) {
		return "Stats for session " + sessionId + " in channel " + channelId;
	}

	// Admin dashboard view
	public Object getAdminChannelView(String channelId) {
		return "Admin view for channel " + channelId;
	}
}
