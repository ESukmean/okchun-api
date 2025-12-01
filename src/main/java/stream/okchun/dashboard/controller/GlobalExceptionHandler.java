package stream.okchun.dashboard.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import stream.okchun.dashboard.config.exception.ExceptionLogger;
import stream.okchun.dashboard.dto.GlobalErrorResponse;
import stream.okchun.dashboard.dto.GlobalResponse;
import stream.okchun.dashboard.exception.OkchunSuperException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final ExceptionLogger exceptionLogger;

	@ExceptionHandler(OkchunSuperException.class)
	public ResponseEntity<@NonNull GlobalResponse<GlobalErrorResponse>> handleException(
			OkchunSuperException e, HttpServletRequest httpReq) {

		exceptionLogger.log(e, e.getStackTrace(), httpReq);

		return new ResponseEntity(new GlobalResponse(false,
				new GlobalErrorResponse(e.getMessage(), e.getLocalizedMessage(), e.getParams())),
				e.getStatus());
	}
}
