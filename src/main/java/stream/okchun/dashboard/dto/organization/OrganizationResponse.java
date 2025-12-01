package stream.okchun.dashboard.dto.organization;

import java.time.OffsetDateTime;

public record OrganizationResponse(
    Long id,
    String name,
    Long ownerUserId,
    String defaultRegion,
    OffsetDateTime createdAt
) {}
