package stream.okchun.dashboard.exception.org;

import org.springframework.http.HttpStatus;
import stream.okchun.dashboard.exception.OkchunSuperException;

public class OrganizationException extends OkchunSuperException {
	public OrganizationException(HttpStatus status, String exception_code, String[] params) {
		super(status, exception_code, params);
	}

	public static OrganizationException DUPLICATED_ORG_ID() {
		return new OrganizationException(HttpStatus.BAD_REQUEST, "ORG_ID_DUPLICATED", null);
	}
	public static OrganizationException BAD_ORG_ID() {
		return new OrganizationException(HttpStatus.BAD_REQUEST, "ORG_ID_BAD", null);
	}
	public static OrganizationException BAD_ORG_NAME() {
		return new OrganizationException(HttpStatus.BAD_REQUEST, "ORG_NAME_BAD", null);
	}

	public static OrganizationException UNKNOWN() {
		return new OrganizationException(HttpStatus.BAD_REQUEST, "UNKNOWN", null);
	}
}
