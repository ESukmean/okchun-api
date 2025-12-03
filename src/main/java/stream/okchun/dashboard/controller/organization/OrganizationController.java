package stream.okchun.dashboard.controller.organization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stream.okchun.dashboard.application.OrganizationApplication;
import stream.okchun.dashboard.config.RustResult;
import stream.okchun.dashboard.dto.GlobalResponse;
import stream.okchun.dashboard.dto.account.LoginResponse;
import stream.okchun.dashboard.dto.account.MyOrganizationInfo;
import stream.okchun.dashboard.dto.organization.CreateOrganizationRequest;
import stream.okchun.dashboard.dto.organization.DetailedOrganizationInfo;
import stream.okchun.dashboard.exception.OkchunSuperException;
import stream.okchun.dashboard.exception.org.OrganizationException;
import stream.okchun.dashboard.service.AccountService;
import stream.okchun.dashboard.service.ApiKeyService;

import java.util.ArrayList;

@RestController
@RequestMapping("/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {
	private final OrganizationApplication orgApplication;
	private final AccountService accountService;
	private final ApiKeyService apiKeyService;

    // Organization CRUD
    @PostMapping
    public GlobalResponse<MyOrganizationInfo> createOrganization(@RequestBody CreateOrganizationRequest body,
																 HttpServletRequest req) {
		var user = accountService.getHttpSessionUser();

		var api_key_result = RustResult.wrap(() -> orgApplication.createOrganization(body.org_id(),
				body.name(),
				user.userId()));
		if (api_key_result.isErr()) {
			if (api_key_result.getException() instanceof OkchunSuperException ex) {
				throw ex;
			}
			throw OrganizationException.UNKNOWN();
		}

		var api_key = api_key_result.getOrDefault(null);
		if (api_key == null) {
			throw OrganizationException.UNKNOWN();
		}

		// 세션에 추가 처리
		var org_info = new MyOrganizationInfo(api_key, body.org_id(), body.name());
		LoginResponse loginSessionData = (LoginResponse)req.getSession().getAttribute("user");
		var organizationInfos = new ArrayList<>(loginSessionData.organizations());
		organizationInfos.add(org_info);

		// immutableList로 다시 변환
		loginSessionData.setOrganizations(organizationInfos.stream().toList());
		req.getSession().setAttribute("user", loginSessionData);

		return new GlobalResponse<>(true, org_info);
    }

    @GetMapping("/{org_id}")
    public GlobalResponse<DetailedOrganizationInfo> getOrganizationDetail(@PathVariable("org_id") String orgId) {
		var key = apiKeyService.getHttpRequestApiKey();
		var org_data = orgApplication.getOrganizationByApiKey(key);
		return GlobalResponse.success(org_data);
    }

    @PatchMapping("/{org_id}")
    public String updateOrganization(@PathVariable("org_id") String orgId, @RequestBody Object body) {
        return "Organization " + orgId + " updated";
    }

    @DeleteMapping("/{org_id}")
    public String deleteOrganization(@PathVariable("org_id") String orgId) {
        return "Organization " + orgId + " deleted";
    }

    // Members & roles
    @GetMapping("/{org_id}/members")
    public String listMembers(@PathVariable("org_id") String orgId, @RequestParam(required = false) String search, @RequestParam(required = false) String cursor) {
        return "List of members for " + orgId;
    }

    @PostMapping("/{org_id}/members")
    public String addMember(@PathVariable("org_id") String orgId, @RequestBody Object body) {
        return "Member added to " + orgId;
    }

    @PatchMapping("/{org_id}/members/{member_id}")
    public String changeMemberRole(@PathVariable("org_id") String orgId, @PathVariable("member_id") String memberId, @RequestBody Object body) {
        return "Role changed for member " + memberId + " in " + orgId;
    }

    @DeleteMapping("/{org_id}/members/{member_id}")
    public String removeMember(@PathVariable("org_id") String orgId, @PathVariable("member_id") String memberId) {
        return "Member " + memberId + " removed from " + orgId;
    }

    // Invites by org
    @PostMapping("/{org_id}/invites")
    public String createInvite(@PathVariable("org_id") String orgId, @RequestBody Object body) {
        return "Invite created for " + orgId;
    }

    @GetMapping("/{org_id}/invites")
    public String listInvites(@PathVariable("org_id") String orgId, @RequestParam(required = false) String cursor) {
        return "List of invites for " + orgId;
    }

    @DeleteMapping("/{org_id}/invites/{invite_id}")
    public String cancelInvite(@PathVariable("org_id") String orgId, @PathVariable("invite_id") String inviteId) {
        return "Invite " + inviteId + " cancelled for " + orgId;
    }

    // API keys (for B2B automation)
    @GetMapping("/{org_id}/api-keys")
    public String listApiKeys(@PathVariable("org_id") String orgId) {
        return "List of API keys for " + orgId;
    }

    @PostMapping("/{org_id}/api-keys")
    public String createApiKey(@PathVariable("org_id") String orgId, @RequestBody Object body) {
        return "API key created for " + orgId;
    }

    @DeleteMapping("/{org_id}/api-keys/{key_id}")
    public String revokeApiKey(@PathVariable("org_id") String orgId, @PathVariable("key_id") String keyId) {
        return "API key " + keyId + " revoked for " + orgId;
    }
}
