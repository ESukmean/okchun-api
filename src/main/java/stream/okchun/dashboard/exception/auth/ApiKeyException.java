package stream.okchun.dashboard.exception.auth;

import org.springframework.http.HttpStatus;
import stream.okchun.dashboard.exception.OkchunSuperException;

public class ApiKeyException extends OkchunSuperException {
	public ApiKeyException(HttpStatus status, String exception_code, String[] params) {
		super(status, exception_code, params);
	}

	public static ApiKeyException NO_APIKEY_PROVIDED() {
		return new ApiKeyException(HttpStatus.FORBIDDEN, "API_NO_APIKEY_PROVIDED", null);
	}

	public static ApiKeyException APIKEY_EXPIRED_OR_UNFOUND() {
		return new ApiKeyException(HttpStatus.BAD_REQUEST, "API_KEY_EXPIRED_OR_UNFOUND", null);
	}
}
