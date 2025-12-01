package stream.okchun.dashboard.dto.organization;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiKeyResponse(
    Long id,
    String name,
    String keyPreview,
    List<String> scopes,
    OffsetDateTime expiresAt,
    OffsetDateTime lastUsedAt,
    OffsetDateTime createdAt
) {}
