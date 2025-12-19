package stream.okchun.dashboard.dto.organization;

public record MyOrganizationResponse(
		Long organizationId,
		String organizationName,
		String userRole
) {
}
