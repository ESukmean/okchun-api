package stream.okchun.dashboard.dto.organization;

public record CreateInviteRequest(
    String email,
    String role
) {}
