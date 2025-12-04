package stream.okchun.dashboard.controller.channel;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stream.okchun.dashboard.application.OrganizationApplication;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;
import stream.okchun.dashboard.dto.GlobalResponse;
import stream.okchun.dashboard.dto.channel.ChannelResponse;
import stream.okchun.dashboard.service.AccountService;
import stream.okchun.dashboard.service.ApiKeyService;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ChannelController {
	private final OrganizationApplication orgApplication;
	private final AccountService accountService;
	private final ApiKeyService apiKeyService;

    // Channels
    @PostMapping("/organizations/{org_id}/channels")
    public String createChannel(@PathVariable("org_id") String orgId, @RequestBody Object body) {
		return "create channel";
    }

    @GetMapping("/organizations/{org_id}/channels")
    public GlobalResponse<List<ChannelResponse>> listChannels(@PathVariable("org_id") String orgId,
															  @RequestParam(required = false) String search,
															  @RequestParam(required = false) String state,
															  @RequestParam(required = false) Integer cursor) {
		var key = apiKeyService.getHttpRequestApiKey();
		ChannelStateType channel_state = null;

		if (state != null) {
			switch (state) {
				case "ACTIVE":
					channel_state = ChannelStateType.ACTIVE;
					break;

				case "ARCHIVED":
					channel_state = ChannelStateType.ARCHIVED;
					break;

				default:
					break;
			}
		}

		var org_data = orgApplication.listChannelsByApiKey(key, search, channel_state, cursor);
		return GlobalResponse.success(org_data);
    }

    @GetMapping("/organizations/{org_id}/channels/{channel_id}")
    public String getChannelDetail(@PathVariable("org_id") String orgId, @PathVariable("channel_id") String channelId) {
        return "Channel detail for " + channelId + " in organization " + orgId;
    }

    @PatchMapping("/organizations/{org_id}/channels/{channel_id}")
    public String updateChannel(@PathVariable("org_id") String orgId, @PathVariable("channel_id") String channelId, @RequestBody Object body) {
        return "Channel " + channelId + " updated in organization " + orgId;
    }

    @DeleteMapping("/organizations/{org_id}/channels/{channel_id}")
    public String archiveChannel(@PathVariable("org_id") String orgId, @PathVariable("channel_id") String channelId) {
        return "Channel " + channelId + " archived in organization " + orgId;
    }

    // Channel settings and extensions
    @GetMapping("/channels/{channel_id}/settings")
    public String getChannelSettings(@PathVariable("channel_id") String channelId) {
        return "Settings for channel " + channelId;
    }

    @PatchMapping("/channels/{channel_id}/settings")
    public String updateChannelSettings(@PathVariable("channel_id") String channelId, @RequestBody Object body) {
        return "Settings updated for channel " + channelId;
    }

    // Extensions
    @GetMapping("/channels/{channel_id}/extensions")
    public String listExtensions(@PathVariable("channel_id") String channelId) {
        return "Extensions for channel " + channelId;
    }

    @PostMapping("/channels/{channel_id}/extensions")
    public String createExtension(@PathVariable("channel_id") String channelId, @RequestBody Object body) {
        return "Extension created for channel " + channelId;
    }

    @GetMapping("/channels/{channel_id}/extensions/{extension_id}")
    public String getExtensionDetail(@PathVariable("channel_id") String channelId, @PathVariable("extension_id") String extensionId) {
        return "Extension " + extensionId + " detail for channel " + channelId;
    }

    @PatchMapping("/channels/{channel_id}/extensions/{extension_id}")
    public String updateExtension(@PathVariable("channel_id") String channelId, @PathVariable("extension_id") String extensionId, @RequestBody Object body) {
        return "Extension " + extensionId + " updated for channel " + channelId;
    }

    @DeleteMapping("/channels/{channel_id}/extensions/{extension_id}")
    public String removeExtension(@PathVariable("channel_id") String channelId, @PathVariable("extension_id") String extensionId) {
        return "Extension " + extensionId + " removed from channel " + channelId;
    }

    @PostMapping("/channels/{channel_id}/extensions/{extension_id}/invoke")
    public String invokeExtension(@PathVariable("channel_id") String channelId, @PathVariable("extension_id") String extensionId, @RequestBody Object body) {
        return "Extension " + extensionId + " invoked for channel " + channelId;
    }

    // Sessions (per broadcast)
    @PostMapping("/channels/{channel_id}/sessions")
    public String createSession(@PathVariable("channel_id") String channelId, @RequestBody Object body) {
        return "Session created for channel " + channelId;
    }

    @GetMapping("/channels/{channel_id}/sessions")
    public String listSessions(@PathVariable("channel_id") String channelId,
                               @RequestParam(required = false) String state,
                               @RequestParam(required = false) String cursor) {
        return "List of sessions for channel " + channelId;
    }

    @GetMapping("/channels/{channel_id}/sessions/{session_id}")
    public String getSessionDetail(@PathVariable("channel_id") String channelId, @PathVariable("session_id") String sessionId) {
        return "Session " + sessionId + " detail for channel " + channelId;
    }

    @PostMapping("/channels/{channel_id}/sessions/{session_id}/stop")
    public String stopSession(@PathVariable("channel_id") String channelId, @PathVariable("session_id") String sessionId) {
        return "Session " + sessionId + " stopped for channel " + channelId;
    }

    @GetMapping("/channels/{channel_id}/sessions/{session_id}/stats")
    public String getSessionStats(@PathVariable("channel_id") String channelId, @PathVariable("session_id") String sessionId) {
        return "Stats for session " + sessionId + " in channel " + channelId;
    }

    // Admin dashboard view
    @GetMapping("/channels/{channel_id}/admin")
    public String getAdminChannelView(@PathVariable("channel_id") String channelId) {
        return "Admin view for channel " + channelId;
    }
}
