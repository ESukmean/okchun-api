package stream.okchun.dashboard.dto.account;

public record TokenRefreshResponse(
    String accessToken,
    String refreshToken
) {}
