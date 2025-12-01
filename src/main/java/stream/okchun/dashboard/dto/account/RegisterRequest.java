package stream.okchun.dashboard.dto.account;

import java.util.Optional;

public record RegisterRequest(
    String email,
    String password,
    String name,
    Optional<String> locale,
    Optional<String> timeZone
) {

}
