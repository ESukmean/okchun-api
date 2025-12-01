package stream.okchun.dashboard.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;

import static stream.okchun.dashboard.config.exception.ExceptionLogger.STATIC_LOG;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RustResult<T> {
	private final T data;
	private final Exception exception;

	public static <T> RustResult<T> success(T data) {
		return new RustResult<>(data, null);
	}

	public static <T> RustResult<T> fail(Exception exception) {
		return new RustResult<>(null, exception);
	}

	public static <T> RustResult<T> wrap(Callable<T> func) {
		try {
			return new RustResult<>(func.call(), null);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			STATIC_LOG.accept(e, sw.toString());

			return new RustResult<>(null, e);
		}
	}

	public boolean isOk() {
		return exception == null;
	}

	public boolean isErr() {
		return exception != null;
	}

	public T getOrThrow() throws Exception {
		if (exception != null) {
			throw exception;
		}
		return data;
	}

	public T getOrDefault(T defaultValue) {
		return isOk() ? data : defaultValue;
	}

	public Exception getException() {
		return exception;
	}
}
