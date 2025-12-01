package stream.okchun.dashboard.dto.organization;

import java.time.OffsetDateTime;

public record MemberResponse(
    Long memberId,
    Long userId,
    String userName,
    String userEmail,
    String role,
    boolean isActive,
    OffsetDateTime joinedAt
) {}
