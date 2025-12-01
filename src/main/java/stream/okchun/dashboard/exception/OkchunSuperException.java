package stream.okchun.dashboard.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class OkchunSuperException extends RuntimeException {
	@Getter
	private final HttpStatus status;

	@Getter
	private final String[] params;
	public static final String[] EMPTY_PARAM = new String[] {};

	public OkchunSuperException(HttpStatus status, String exception_code, String[] params) {
		super(exception_code);
		this.status = status;
		this.params = params;
	}
}

