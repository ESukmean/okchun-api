package stream.okchun.dashboard.dto.account;

import stream.okchun.dashboard.database.entity.org.OrganizationMember;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public record LoginResponse(
	long userId,
	String name,
    TimeZone timezone,
	Locale locale,
	List<MyOrganizationInfo> organizations
) {
	public static LoginResponse of(long userId, String name, TimeZone timezone, Locale locale,
								   List<OrganizationMember> organizations) {
		return new LoginResponse(userId, name, timezone, locale,
				organizations.stream().map(MyOrganizationInfo::of).toList());
	}

	public LoginResponse setOrganizations(List<MyOrganizationInfo> organizations) {
		return new LoginResponse(userId, name, timezone, locale, organizations);
	}

	public LoginResponse setName(String name) {
		return new LoginResponse(userId, name, timezone, locale, organizations);
	}

	public LoginResponse setTimezone(TimeZone tz) {
		return new LoginResponse(userId, name, tz, locale, organizations);
	}

	public LoginResponse setLocale(Locale locale) {
		return new LoginResponse(userId, name, timezone, locale, organizations);
	}
}