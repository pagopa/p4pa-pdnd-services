package it.gov.pagopa.payhub.pdnd.connector.pdnd;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;

public interface PdndService {
    PdndAuthData generateToken(PdndServiceIntegratedConfig pdndServiceIntegratedConfig);
}
