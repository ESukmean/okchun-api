package stream.okchun.dashboard.config.exception;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;


public final class LogJson {
	private static final ObjectMapper MAPPER = JsonMapper.builder()
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).build();

	private LogJson() {}

	public static String safe(Object value) {
		if (value == null) return "null";
		try {
			return MAPPER.writeValueAsString(value);
		} catch (JacksonException e) {
			return String.valueOf(value);
		}
	}
}
