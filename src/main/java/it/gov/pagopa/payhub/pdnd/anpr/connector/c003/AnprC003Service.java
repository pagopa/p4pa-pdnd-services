package it.gov.pagopa.payhub.pdnd.anpr.connector.c003;

import it.gov.pagopa.anpr.c003.dto.generated.RispostaE002OK;

public interface AnprC003Service {
    RispostaE002OK getUserData(String idAnpr, Long organizationId, String subUnitCode, String accessToken);
}

