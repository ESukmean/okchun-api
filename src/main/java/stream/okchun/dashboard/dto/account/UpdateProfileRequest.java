package stream.okchun.dashboard.dto.account;

import java.util.Optional;

public record UpdateProfileRequest(
    Optional<String> name,
    Optional<String> locale,
    Optional<String> timeZone
) {}
