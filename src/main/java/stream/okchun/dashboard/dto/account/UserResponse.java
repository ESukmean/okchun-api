package stream.okchun.dashboard.dto.account;

import stream.okchun.dashboard.dto.organization.MyOrganizationResponse;
import java.util.List;

public record UserResponse(
    Long id,
    String email,
    String name,
    String locale,
    String timeZone,
    Long activeOrganizationId,
    List<MyOrganizationResponse> organizations
) {}
