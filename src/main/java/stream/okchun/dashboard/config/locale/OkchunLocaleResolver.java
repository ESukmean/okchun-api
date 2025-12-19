package stream.okchun.dashboard.config.locale;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

public class OkchunLocaleResolver extends AcceptHeaderLocaleResolver {
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		var locale_cookie = request.getCookies() == null ? null :
				Arrays.stream(request.getCookies())
						.filter(cookie -> cookie.getName().equals("locale"))
						.findFirst()
						.orElse(null);

		if (locale_cookie != null) {
			var locale = Locale.of(locale_cookie.getValue());
			if (locale != null) {
				return locale;
			}
		}

		return super.resolveLocale(request);
	}
}
