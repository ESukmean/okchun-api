package stream.okchun.dashboard.dto.billing;

public record CreateTopupResponse(
    Long topupId,
    String checkoutUrl
) {}
