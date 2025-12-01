package stream.okchun.dashboard.dto.billing;

import java.math.BigDecimal;

public record CreateTopupRequest(
    BigDecimal amountCredit,
    String returnUrl
) {}
