package stream.okchun.dashboard.dto;

public record GlobalErrorResponse(
		String error_id,
		String error_msg,
		String[] parameter
) {
}
