package stream.okchun.dashboard.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stream.okchun.dashboard.config.RustResult;
import stream.okchun.dashboard.database.entity.auth.ApiKey;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.org.Organization;
import stream.okchun.dashboard.database.entity.org.OrganizationMember;
import stream.okchun.dashboard.database.entity.org.OrganizationMemberRole;
import stream.okchun.dashboard.database.repos.org.OrganizationMemberRepository;
import stream.okchun.dashboard.database.repos.org.OrganizationRepository;
import stream.okchun.dashboard.exception.org.OrganizationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
	private final OrganizationRepository organizationRepository;
	private final OrganizationMemberRepository organizationMemberRepository;

    // Organization CRUD
    public Organization createOrganization(String org_id, String org_name, User owner) throws OrganizationException {
		if (!org_id.matches("^[a-zA-Z0-9_\\-]*$") || org_id.length() > 16 || org_id.isEmpty()) {
			throw OrganizationException.BAD_ORG_ID();
		}

		org_name = org_name.trim();
		if (org_name.length() > 16 || org_name.isEmpty()) {
			throw OrganizationException.BAD_ORG_NAME();
		}

        Organization organization = new Organization(null, org_name, owner, null, null, null, null, org_id);
		RustResult<Organization> org_result =
				RustResult.wrap(() -> organizationRepository.save(organization));


		if (org_result.isErr()) {
			if (org_result.getException().getMessage().contains("duplicate key value")) {
				throw OrganizationException.DUPLICATED_ORG_ID();
			}
		}

		return org_result.getOrDefault(null);
    }

    public Object listOrganizations() {
        return "List of organizations";
    }

    public Object getOrganizationDetail(String orgId) {
        return "Organization detail for " + orgId;
    }

    public Object updateOrganization(String orgId, Object body) {
        return "Organization " + orgId + " updated";
    }

    public Object deleteOrganization(String orgId) {
        return "Organization " + orgId + " deleted";
    }

    // Members & roles
	public List<OrganizationMember> listOrganizationOfUser(@NonNull Long userId) {
		return organizationMemberRepository.findAllByUserId(userId);
	}
    public Object listMembers(String orgId, String search, String cursor) {
        return "List of members for " + orgId;
    }

    public OrganizationMember addMember(Organization org, User user, ApiKey apiKey, OrganizationMemberRole role) {
		OrganizationMember member =  new OrganizationMember(null, org, user, role, true, null, null, apiKey);
		return organizationMemberRepository.save(member);
    }

    public Object changeMemberRole(String orgId, String memberId, Object body) {
        return "Role changed for member " + memberId + " in " + orgId;
    }

    public Object removeMember(String orgId, String memberId) {
        return "Member " + memberId + " removed from " + orgId;
    }

    // Invites by org
    public Object createInvite(String orgId, Object body) {
        return "Invite created for " + orgId;
    }

    public Object listInvites(String orgId, String cursor) {
        return "List of invites for " + orgId;
    }

    public Object cancelInvite(String orgId, String inviteId) {
        return "Invite " + inviteId + " cancelled for " + orgId;
    }

    // API keys (for B2B automation)
    public Object listApiKeys(String orgId) {
        return "List of API keys for " + orgId;
    }

    public Object createApiKey(String orgId, Object body) {
        return "API key created for " + orgId;
    }

    public Object revokeApiKey(String orgId, String keyId) {
        return "API key " + keyId + " revoked for " + orgId;
    }
}
