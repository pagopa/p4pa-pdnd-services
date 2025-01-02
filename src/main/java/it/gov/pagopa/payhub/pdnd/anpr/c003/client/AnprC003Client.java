package it.gov.pagopa.payhub.pdnd.anpr.c003.client;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK;

public interface AnprC003Client {
  RispostaE002OK getUserData(RichiestaE002 request);
}
