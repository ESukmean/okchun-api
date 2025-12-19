package stream.okchun.dashboard.dto.account;

public record PasswordResetConfirmRequest(
		String email,
		String token,
		String newPassword
) {
}
