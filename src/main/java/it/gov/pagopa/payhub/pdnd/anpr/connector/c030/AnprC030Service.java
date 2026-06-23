package it.gov.pagopa.payhub.pdnd.anpr.connector.c030;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;

public interface AnprC030Service {
    RispostaE002OK getIdAnprFromFc(String fiscalCode, Long organizationId, String subUnitCode, String accessToken);
}

