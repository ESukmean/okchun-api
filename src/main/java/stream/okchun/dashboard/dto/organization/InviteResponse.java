package stream.okchun.dashboard.dto.organization;

import java.time.OffsetDateTime;

public record InviteResponse(
    Long id,
    String email,
    String role,
    String status,
    OffsetDateTime expiresAt,
    OffsetDateTime createdAt
) {}
