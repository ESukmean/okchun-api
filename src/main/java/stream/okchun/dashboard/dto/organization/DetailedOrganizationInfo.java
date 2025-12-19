package stream.okchun.dashboard.dto.organization;

import jakarta.annotation.Nullable;
import stream.okchun.dashboard.database.entity.media.Channel;
import stream.okchun.dashboard.database.entity.org.Organization;

import java.time.OffsetDateTime;
import java.util.List;

public record DetailedOrganizationInfo(
		String org_id,
		String name,
		String defaultRegion,
		OffsetDateTime createdAt,
		@Nullable List<Channel> currentChannels
) {
	public static DetailedOrganizationInfo from(Organization org, List<Channel> currentChannels) {
		return new DetailedOrganizationInfo(org.getOrgId(), org.getName(), org.getDefaultRegion(),
				org.getCreatedAt(), currentChannels);
	}
}
