package stream.okchun.dashboard.dto.billing;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record UsageRecordResponse(
		Long usageId,
		Long channelId,
		String channelName,
		Long sessionId,
		Long poolId,
		String poolName,
		OffsetDateTime from,
		OffsetDateTime to,
		Long bytesUsed,
		BigDecimal creditCharged
) {
}
