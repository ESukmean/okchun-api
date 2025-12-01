package stream.okchun.dashboard.dto.organization;

public record AddMemberRequest(
    String email,
    String role
) {}
