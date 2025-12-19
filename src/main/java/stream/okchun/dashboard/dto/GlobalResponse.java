package stream.okchun.dashboard.dto;

public record GlobalResponse<T>(
		boolean status,
		T data
) {
	public static <T> GlobalResponse<T> success(T data) {
		return new GlobalResponse<>(true, data);
	}

	public static <T> GlobalResponse<T> fail(T data) {
		return new GlobalResponse<>(false, data);
	}
}
