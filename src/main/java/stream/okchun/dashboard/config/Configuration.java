package stream.okchun.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import stream.okchun.dashboard.config.locale.OkchunLocaleResolver;

@org.springframework.context.annotation.Configuration
@EnableRetry
public class Configuration {

	@Bean
	public LocaleResolver localeResolver() {
		return new OkchunLocaleResolver();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
