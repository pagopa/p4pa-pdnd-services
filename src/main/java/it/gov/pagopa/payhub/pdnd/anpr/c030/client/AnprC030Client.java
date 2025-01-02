package it.gov.pagopa.payhub.pdnd.anpr.c030.client;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;

public interface AnprC030Client {
  RispostaE002OK getIdAnprFromFc(RichiestaE002 request);
}
