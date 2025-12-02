package stream.okchun.dashboard.exception.auth;

import org.springframework.http.HttpStatus;
import stream.okchun.dashboard.exception.OkchunSuperException;

public class RegisterException extends OkchunSuperException {
	public  RegisterException(HttpStatus status, String exception_code, String[] params) {
		super(status, exception_code, params);
	}

	public static RegisterException DUPLICATED() {
		return new RegisterException(HttpStatus.BAD_REQUEST, "DUPLICATED_REGISTER", OkchunSuperException.EMPTY_PARAM);
	}
	public static RegisterException UNKNOWN() {
		return new RegisterException(HttpStatus.BAD_REQUEST, "UNKNOWN", OkchunSuperException.EMPTY_PARAM);
	}
}
