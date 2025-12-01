package stream.okchun.dashboard.dto.billing;

import java.math.BigDecimal;
import java.util.Optional;

public record UpdateCreditSettingsRequest(
    Optional<BigDecimal> lowBalanceThreshold,
    Optional<Boolean> autoTopupEnabled,
    Optional<BigDecimal> autoTopupAmount
) {}
