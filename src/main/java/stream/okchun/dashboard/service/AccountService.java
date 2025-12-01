package stream.okchun.dashboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.config.RustResult;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.repos.auth.UserRepository;
import stream.okchun.dashboard.dto.HttpClientInformation;
import stream.okchun.dashboard.dto.account.RegisterRequest;
import stream.okchun.dashboard.exception.auth.RegisterException;

import java.util.TimeZone;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

    public boolean register(RegisterRequest data, HttpClientInformation client) throws RegisterException {
		User user = new User(null, passwordEncoder.encode(data.password()), data.name(),
				data.locale().orElse(client.locale().toString()), TimeZone.getDefault().getDisplayName(), null, null, null,
				null);

		RustResult<User> result = RustResult.wrap(() -> userRepository.save(user));
		log.debug("insert = {}", result.getException());
		return result.isOk();
	}

    public Object login(Object body) {
        return "User logged in";
    }

    public Object logout() {
        return "User logged out";
    }

    public Object refreshToken() {
        return "Token refreshed";
    }

    // Email & password
    public Object requestVerifyEmail() {
        return "Verification email sent";
    }

    public Object confirmVerifyEmail(Object body) {
        return "Email confirmed";
    }

    public Object requestPasswordReset() {
        return "Password reset email sent";
    }

    public Object confirmPasswordReset(Object body) {
        return "Password reset confirmed";
    }

    // Profile / multi-org
    public Object getMyProfile() {
        return "User profile details";
    }

    public Object updateMyProfile(Object body) {
        return "Profile updated";
    }

    public Object changeMyPassword(Object body) {
        return "Password changed";
    }

    public Object setActiveOrganization(Object body) {
        return "Active organization set";
    }

    // Invites
    public Object listMyInvites() {
        return "List of user invites";
    }

    public Object acceptInvite(String inviteCode) {
        return "Invite accepted";
    }

    // Account lifecycle
    public Object closeMyAccount() {
        return "Account closed";
    }
}
