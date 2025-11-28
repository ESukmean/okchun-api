package stream.okchun.dashboard.controller.account;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/account")
public class AccountController {

    // Core auth
    @PostMapping("/register")
    public String register(@RequestBody Object body) {
        return "Register successful";
    }

    @PostMapping("/login")
    public String login(@RequestBody Object body) {
        return "Login successful";
    }

    @PostMapping("/logout")
    public String logout() {
        return "Logout successful";
    }

    @PostMapping("/token/refresh")
    public String refreshToken() {
        return "Token refreshed";
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
    public String getMyProfile() {
        return "My profile details";
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
