package stream.okchun.dashboard.dto;

import lombok.AllArgsConstructor;

public record GlobalResponse<T> (
		boolean status,
		T data
) {

}
