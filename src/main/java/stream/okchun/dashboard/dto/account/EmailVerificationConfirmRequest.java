package stream.okchun.dashboard.dto.account;

public record EmailVerificationConfirmRequest(
		String email,
		String token
) {
}
