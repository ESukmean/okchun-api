package stream.okchun.dashboard.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import stream.okchun.dashboard.database.entity.auth.ApiKey;
import stream.okchun.dashboard.database.entity.auth.ApiKeySubjectType;
import stream.okchun.dashboard.database.entity.org.Organization;
import stream.okchun.dashboard.database.repos.auth.ApiKeyRepository;
import stream.okchun.dashboard.dto.account.LoginResponse;
import stream.okchun.dashboard.exception.auth.ApiKeyException;
import stream.okchun.dashboard.exception.auth.LoginException;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
	private final ApiKeyRepository apiKeyRepository;

	public ApiKey createApiKey(Organization org, ApiKeySubjectType type, long subjet_ref, List<String> scope,
							   String note){
		String apiKey = "";
		do {
			apiKey = generateApiKey();
		} while (checkIfKeyExists(apiKey));

		ApiKey key = new ApiKey(null, apiKey, type, subjet_ref, scope, true, null, null, null, null,
				note, org);
		return apiKeyRepository.save(key);
	}

	public ApiKey getHttpRequestApiKey(){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			var api_key = request.getHeader("X-API-KEY");
			if (api_key == null) {
				throw ApiKeyException.NO_APIKEY_PROVIDED();
			}

			var api_fetch = apiKeyRepository.findByKey(api_key);
			if (api_fetch == null || api_fetch.getIsActive() == false || api_fetch.getRevokedAt() != null) {
				throw ApiKeyException.APIKEY_EXPIRED_OR_UNFOUND();
			}

			return api_fetch;
		}
		throw LoginException.NOT_LOGGED_IN();
	}

	private String generateApiKey() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 48) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		return salt.toString();
	}
	public boolean checkIfKeyExists(String apiKey){
		return apiKeyRepository.existsByKey(apiKey);
	}
}
