package stream.okchun.dashboard.service;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    // Core auth
    public Object register(Object body) {
        return "Account registered";
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
