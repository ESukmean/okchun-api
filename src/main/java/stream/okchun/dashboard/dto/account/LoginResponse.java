package stream.okchun.dashboard.dto.account;

public record LoginResponse(
    String accessToken,
    String refreshToken
) {}
