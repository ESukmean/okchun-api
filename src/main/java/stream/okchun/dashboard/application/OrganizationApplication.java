package stream.okchun.dashboard.application;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.database.entity.auth.ApiKey;
import stream.okchun.dashboard.database.entity.auth.ApiKeySubjectType;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;
import stream.okchun.dashboard.database.entity.org.OrganizationMemberRole;
import stream.okchun.dashboard.dto.billing.CreditBalanceResponse;
import stream.okchun.dashboard.dto.billing.TopupHistoryResponse;
import stream.okchun.dashboard.dto.channel.ChannelResponse;
import stream.okchun.dashboard.dto.organization.DetailedOrganizationInfo;
import stream.okchun.dashboard.exception.OkchunSuperException;
import stream.okchun.dashboard.service.ApiKeyService;
import stream.okchun.dashboard.service.BillingService;
import stream.okchun.dashboard.service.ChannelService;
import stream.okchun.dashboard.service.OrganizationService;
import stream.okchun.dashboard.service.billing.tx.BillingAccountType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationApplication {
	private final OrganizationService orgService;
	private final ChannelService channelService;
	private final BillingService billingService;

	private final ApiKeyService apiKeyService;
	private final EntityManager em;

	@Transactional
	public String createOrganization(String org_id, String name, long userId) throws OkchunSuperException {
		User user = em.getReference(User.class, userId);
		var org = orgService.createOrganization(org_id, name, user);
		var key = apiKeyService.createApiKey(org, ApiKeySubjectType.ORG, userId, List.of(), "");

		orgService.addMember(org, user, key, OrganizationMemberRole.OWNER);
		billingService.createBillingAccount(BillingAccountType.ORG, org.getId(), "USD", "");
		
		return key.getKey();
	}


	public DetailedOrganizationInfo getOrganizationByApiKey(ApiKey apiKey) {
		var org = apiKey.getOrg();
		var channels = channelService.listChannels(org.getId(), null, ChannelStateType.ACTIVE, null);
		return DetailedOrganizationInfo.from(org, channels);
	}

	public List<ChannelResponse> listChannelsByApiKey(ApiKey apiKey, String search, ChannelStateType state,
													  Integer cursor) {
		var org = apiKey.getOrg();
		return channelService.listChannels(org.getId(), search, state, cursor).stream().map(
				ChannelResponse::of).toList();
	}

	public List<CreditBalanceResponse> getBalance(ApiKey apiKey) {
		var org = apiKey.getOrg();
		var billings = billingService.getBillingAccount(BillingAccountType.ORG, org.getId(), null);

		return billings.stream().map(v -> CreditBalanceResponse.of(v.getBalance(), v.getCurrency())).toList();
	}

	public Page<TopupHistoryResponse> listTopUp(ApiKey apiKey, @Nullable Integer page) {
		var histories = billingService.listBillingInvoice(apiKey.getOrg().getId(), page);

		return histories.map(TopupHistoryResponse::from);
	}
}
