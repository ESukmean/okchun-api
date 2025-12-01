package stream.okchun.dashboard.dto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.time.OffsetDateTime;
import java.util.Locale;

public record HttpClientInformation(
	Cookie[] cookie,
	String remoteAddr,
	OffsetDateTime reqTime,
	Locale locale
) {
	public static HttpClientInformation of(HttpServletRequest req) {
		return new HttpClientInformation(req.getCookies(), req.getRemoteAddr(), OffsetDateTime.now(), req.getLocale());
	}
}
