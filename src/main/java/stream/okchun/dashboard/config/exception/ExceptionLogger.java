package stream.okchun.dashboard.config.exception;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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
		RequestAttributes ctx = RequestContextHolder.currentRequestAttributes();
		String trace_id = (String) ctx.getAttribute("REQUEST_TRACE_ID", 0);

		log.error("server_id = {}, message = {}, data = {}, stack = {}, req = {}",  trace_id, ex.getMessage(),
				data,	ex,
				req);
	}
}

