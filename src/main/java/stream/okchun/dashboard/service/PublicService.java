package stream.okchun.dashboard.service;

import org.springframework.stereotype.Service;

@Service
public class PublicService {

    public Object getPublicChannelMetadata(String channelId) {
        return "Public metadata for channel " + channelId;
    }

    public Object getPublicChannelPlayData(String channelId) {
        return "Play data for channel " + channelId;
    }
}
