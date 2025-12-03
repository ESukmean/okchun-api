package stream.okchun.dashboard.controller.account;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stream.okchun.dashboard.application.AccountApplication;
import stream.okchun.dashboard.dto.GlobalResponse;
import stream.okchun.dashboard.dto.HttpClientInformation;
import stream.okchun.dashboard.dto.account.LoginRequest;
import stream.okchun.dashboard.dto.account.LoginResponse;
import stream.okchun.dashboard.dto.account.RegisterRequest;
import stream.okchun.dashboard.exception.auth.RegisterException;
import stream.okchun.dashboard.service.AccountService;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountController {
	private final AccountApplication application;
	private final AccountService accountService;

	// Core auth
	@PostMapping("/register")
	public GlobalResponse<Void> register(@RequestBody RegisterRequest registerRequest,
										 HttpServletRequest httpReq) {
		boolean is_success = application.register(registerRequest, HttpClientInformation.of(httpReq));
		if (!is_success) {
			throw RegisterException.UNKNOWN();
		}

		return new GlobalResponse<>(true, null);
	}

	@PostMapping("/login")
	public GlobalResponse<LoginResponse> login(@RequestBody LoginRequest body, HttpServletRequest httpReq) {
		var login = application.login(body, HttpClientInformation.of(httpReq));
		httpReq.getSession().setAttribute("user", login);

		return new GlobalResponse<>(true, login);
	}

	@PostMapping("/logout")
	public void logout(HttpServletRequest httpReq) {
		var session = httpReq.getSession(false);
		if (session == null) {
			return;
		}

		session.removeAttribute("user");
	}

	// Email & password
	@PostMapping("/verify-email/request")
	public String requestVerifyEmail() {
		return "Verify email request sent";
	}

	@PostMapping("/verify-email/confirm")
	public String confirmVerifyEmail(@RequestBody Object body) {
		return "Email verified";
	}

	@PostMapping("/password/reset/request")
	public String requestPasswordReset() {
		return "Password reset request sent";
	}

	@PostMapping("/password/reset/confirm")
	public String confirmPasswordReset(@RequestBody Object body) {
		return "Password reset confirmed";
	}

	// Profile / multi-org
	@GetMapping("/me")
	public GlobalResponse<LoginResponse> getMyProfile() {
		return GlobalResponse.success(accountService.getHttpSessionUser());
	}

	@PatchMapping("/me")
	public String updateMyProfile(@RequestBody Object body) {
		return "Profile updated";
	}

	@PatchMapping("/me/password")
	public String changeMyPassword(@RequestBody Object body) {
		return "Password changed";
	}

	@PatchMapping("/me/active-organization")
	public String setActiveOrganization(@RequestBody Object body) {
		return "Active organization set";
	}

	// Invites
	@GetMapping("/invites")
	public String listMyInvites() {
		return "List of my invites";
	}

	@PostMapping("/invites/{invite_code}/accept")
	public String acceptInvite(@PathVariable("invite_code") String inviteCode) {
		return "Invite accepted";
	}

	// Account lifecycle
	@DeleteMapping("/me")
	public String closeMyAccount() {
		return "Account closed";
	}
}
