package stream.okchun.dashboard.service;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.config.RustResult;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.repos.auth.UserRepository;
import stream.okchun.dashboard.dto.HttpClientInformation;
import stream.okchun.dashboard.dto.account.RegisterRequest;
import stream.okchun.dashboard.exception.auth.LoginException;
import stream.okchun.dashboard.exception.auth.RegisterException;

import java.util.TimeZone;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

    public boolean register(RegisterRequest data, HttpClientInformation client) throws RegisterException {
		User user = new User(null, passwordEncoder.encode(data.password()), data.email(), data.name(),
				data.locale().orElse(client.locale().toString()), TimeZone.getDefault().getID(), null, null,
				null,
				null);

		RustResult<User> result = RustResult.wrap(() -> userRepository.save(user));
		return result.isOk();
	}

    public @NonNull User login(String email, String password) throws LoginException {
        var user_result = RustResult.wrap(() -> this.userRepository.findByEmail(email));
		if (user_result.isErr()) {
			throw LoginException.UNKNOWN();
		}

		var user = user_result.getOrDefault(null);
		if (user == null) {
			throw LoginException.NO_ACCOUNT_FOUND();
		}

		if (this.passwordEncoder.matches(password, user.getPasswordHash())) {
			return user;
		}

		throw LoginException.PASSWORD_INCORRECT();
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
