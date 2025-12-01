package stream.okchun.dashboard.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.dto.HttpClientInformation;
import stream.okchun.dashboard.dto.account.RegisterRequest;
import stream.okchun.dashboard.exception.OkchunSuperException;
import stream.okchun.dashboard.service.AccountService;

@Service
@RequiredArgsConstructor
public class AccountApplication {
	private final AccountService accountService;

	public void register(RegisterRequest data, HttpClientInformation client) throws OkchunSuperException {
		accountService.register(data, client);
	}
}
