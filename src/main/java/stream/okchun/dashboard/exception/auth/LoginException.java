package stream.okchun.dashboard.exception.auth;

import org.springframework.http.HttpStatus;
import stream.okchun.dashboard.exception.OkchunSuperException;

public class LoginException extends OkchunSuperException {
	public LoginException(HttpStatus status, String exception_code, String[] params) {
		super(status, exception_code, params);
	}

	public static LoginException NO_ACCOUNT_FOUND() {
		return new LoginException(HttpStatus.BAD_REQUEST, "ACC_NO_FOUND",
				OkchunSuperException.EMPTY_PARAM);
	}
	public static LoginException PASSWORD_INCORRECT() {
		return new LoginException(HttpStatus.BAD_REQUEST, "ACC_PASSWORD_INCORRECT",
				OkchunSuperException.EMPTY_PARAM);
	}

	public static LoginException UNKNOWN() {
		return new LoginException(HttpStatus.BAD_REQUEST, "UNKNOWN", OkchunSuperException.EMPTY_PARAM);
	}

	public static LoginException NOT_LOGGED_IN() {
		return new LoginException(HttpStatus.BAD_REQUEST, "LOGIN_NOT_LOGGED",
				OkchunSuperException.EMPTY_PARAM);
	}
}
