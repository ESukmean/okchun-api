package stream.okchun.dashboard.service;

import org.springframework.stereotype.Service;

@Service
public class UsageService {

	// Usage / analytics
	public Object getUsageSummary(String orgId, String from, String to) {
		return "Usage summary for organization " + orgId;
	}

	public Object getChannelUsage(String orgId, String from, String to, String cursor) {
		return "Channel usage for organization " + orgId;
	}

	public Object getPoolUsage(String orgId, String from, String to, String cursor) {
		return "Pool usage for organization " + orgId;
	}

	// Audit logs
	public Object getAuditLogs(String orgId, String actor, String action, String from, String to,
							   String cursor) {
		return "Audit logs for organization " + orgId;
	}
}
