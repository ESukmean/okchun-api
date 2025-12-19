package stream.okchun.dashboard.dto.organization;

import java.util.Optional;

public record UpdateOrganizationRequest(
		Optional<String> name,
		Optional<String> defaultRegion
) {
}
