package stream.okchun.dashboard.dto.account;

import stream.okchun.dashboard.database.entity.org.Organization;
import stream.okchun.dashboard.database.entity.org.OrganizationMember;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public record LoginResponse(
	String name,
    TimeZone timezone,
	Locale locale,
	List<OrganizationInfo> organizations
) {
	public static LoginResponse of(String name, TimeZone timezone, Locale locale, List<OrganizationMember> organizations) {
		return new LoginResponse(name, timezone, locale,
				organizations.stream().map(OrganizationInfo::of).toList());
	}
}

record OrganizationInfo(
		String api_key,
		String name
) {
	public static OrganizationInfo of(OrganizationMember org) {
		return new OrganizationInfo(org.getApiKey().getKey(), org.getOrg().getName());
	}
}