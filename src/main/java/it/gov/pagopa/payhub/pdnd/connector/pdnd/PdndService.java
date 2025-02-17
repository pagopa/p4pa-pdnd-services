package it.gov.pagopa.payhub.pdnd.connector.pdnd;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;

public interface PdndService {
    String generateToken(PdndServiceIntegratedConfig pdndServiceIntegratedConfig);
}
