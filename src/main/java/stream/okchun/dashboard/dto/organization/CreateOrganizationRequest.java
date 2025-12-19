package stream.okchun.dashboard.dto.organization;

import java.util.Optional;

public record CreateOrganizationRequest(
		String name,
		String org_id,
		Optional<String> defaultRegion
) {
}
