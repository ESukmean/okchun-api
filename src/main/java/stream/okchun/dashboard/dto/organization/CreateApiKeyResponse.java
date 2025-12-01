package stream.okchun.dashboard.dto.organization;

import java.time.OffsetDateTime;
import java.util.List;

public record CreateApiKeyResponse(
    Long id,
    String name,
    String apiKey,
    List<String> scopes,
    OffsetDateTime expiresAt,
    OffsetDateTime createdAt
) {}
