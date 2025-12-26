package stream.okchun.dashboard.controller;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import stream.okchun.dashboard.dto.GlobalResponse;
@RestControllerAdvice

public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(MethodParameter returnType,
							Class<? extends HttpMessageConverter<?>> converterType) {

		Class<?> paramType = returnType.getParameterType();

		// 이미 감싼 경우는 다시 감싸지 않기
		if (GlobalResponse.class.isAssignableFrom(paramType)) return false;

		// ResponseEntity는 컨트롤러가 의도적으로 status/header를 제어하는 경우가 많음
		if (ResponseEntity.class.isAssignableFrom(paramType)) return false;

		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body,
								  MethodParameter returnType,
								  MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType,
								  ServerHttpRequest request,
								  ServerHttpResponse response) {

		// void/null 응답도 success로 통일하고 싶으면 이렇게
		return GlobalResponse.success(body);
	}
}
