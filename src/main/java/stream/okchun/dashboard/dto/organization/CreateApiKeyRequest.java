package stream.okchun.dashboard.dto.organization;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public record CreateApiKeyRequest(
    String name,
    List<String> scopes,
    Optional<OffsetDateTime> expiresAt
) {}
