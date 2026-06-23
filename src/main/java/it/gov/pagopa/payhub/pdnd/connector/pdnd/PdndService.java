package it.gov.pagopa.payhub.pdnd.connector.pdnd;

import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;

public interface PdndService {
    PdndAuthData generateToken(PdndServiceType pdndServicesEnum, Long organizationId, String subUnitCode, String accessToken);
}
