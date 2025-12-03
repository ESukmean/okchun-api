package stream.okchun.dashboard.dto.account;

import stream.okchun.dashboard.database.entity.org.OrganizationMember;

public record MyOrganizationInfo(
		String api_key,
		String org_id,
		String name
) {
	public static MyOrganizationInfo of(OrganizationMember org) {
		return new MyOrganizationInfo(org.getApiKey().getKey(), org.getOrg().getOrgId(),
				org.getOrg().getName());
	}
}

