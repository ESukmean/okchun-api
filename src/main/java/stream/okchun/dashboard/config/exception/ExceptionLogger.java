package stream.okchun.dashboard.config.exception;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.function.BiConsumer;

@Slf4j
@Component
public class ExceptionLogger {
	public static BiConsumer<Exception, Object> STATIC_LOG = (ex, data) -> {};

	@PostConstruct
	public void init() {
		STATIC_LOG = (ex, data) -> this.log(ex, data, null);
	}

	public void log(Exception ex, Object data, @Nullable HttpServletRequest req) {
		// 나중에 DB로 들어감. 일단은 출력만 테스트로 해봄
		log.error("message = {}, data = {}, stack = {}, req = {}", ex.getMessage(), data, ex, req);
	}
}

