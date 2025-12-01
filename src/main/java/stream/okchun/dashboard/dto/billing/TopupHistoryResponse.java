package stream.okchun.dashboard.dto.billing;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TopupHistoryResponse(
    Long id,
    BigDecimal amountCredit,
    BigDecimal amountCurrency,
    String currencyCode,
    String status,
    String provider,
    OffsetDateTime createdAt
) {}
