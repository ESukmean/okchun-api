package stream.okchun.dashboard.application;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.database.entity.auth.ApiKey;
import stream.okchun.dashboard.database.entity.auth.ApiKeySubjectType;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.media.ChannelStateType;
import stream.okchun.dashboard.database.entity.org.OrganizationMemberRole;
import stream.okchun.dashboard.dto.channel.ChannelResponse;
import stream.okchun.dashboard.dto.organization.DetailedOrganizationInfo;
import stream.okchun.dashboard.exception.OkchunSuperException;
import stream.okchun.dashboard.service.ApiKeyService;
import stream.okchun.dashboard.service.ChannelService;
import stream.okchun.dashboard.service.OrganizationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationApplication {
	private final OrganizationService orgService;
	private final ChannelService channelService;
	private final ApiKeyService apiKeyService;
	private final EntityManager em;

	@Transactional
	public String createOrganization(String org_id, String name, long userId) throws OkchunSuperException {
		User user = em.getReference(User.class, userId);
		var org = orgService.createOrganization(org_id, name, user);
		var key = apiKeyService.createApiKey(org, ApiKeySubjectType.ORG, userId, List.of(), "");

		orgService.addMember(org, user, key, OrganizationMemberRole.OWNER);
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
}
