package stream.okchun.dashboard.application;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.dto.GlobalResponse;
import stream.okchun.dashboard.dto.HttpClientInformation;
import stream.okchun.dashboard.dto.account.LoginRequest;
import stream.okchun.dashboard.dto.account.LoginResponse;
import stream.okchun.dashboard.dto.account.RegisterRequest;
import stream.okchun.dashboard.exception.OkchunSuperException;
import stream.okchun.dashboard.service.AccountService;
import stream.okchun.dashboard.service.OrganizationService;

import java.util.Locale;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class AccountApplication {
	private final AccountService accountService;
//	private final AuditService
	private final OrganizationService orgService;

	public boolean register(RegisterRequest data, HttpClientInformation client) throws OkchunSuperException {
		return accountService.register(data, client);
	}

	@Transactional
	public LoginResponse login(@RequestBody LoginRequest body, HttpClientInformation client) throws OkchunSuperException {
		@NonNull User user = accountService.login(body.email(), body.password());

		var org = orgService.listOrganizationOfUser(user.getId());

		return LoginResponse.of(user.getId(), user.getName(),
				TimeZone.getTimeZone(user.getTimeZone()),
				StringUtils.parseLocaleString(user.getLocale()), org);
	}
}
