package stream.okchun.dashboard.dto.account;

public record ChangePasswordRequest(
    String currentPassword,
    String newPassword
) {}
