package stream.okchun.dashboard.controller.publicapi;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/public/channels")
public class PublicController {

    @GetMapping("/{channel_id}")
    public String getPublicChannelMetadata(@PathVariable("channel_id") String channelId) {
        return "Public metadata for channel " + channelId;
    }

    @GetMapping("/{channel_id}/play")
    public String getPublicChannelPlayData(@PathVariable("channel_id") String channelId) {
        return "Play data for channel " + channelId;
    }
}
