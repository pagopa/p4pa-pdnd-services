package it.gov.pagopa.payhub.pdnd.anpr.c030.client;

import it.gov.pagopa.payhub.pdnd.anpr.c030.dto.RichiestaDTO;
import it.gov.pagopa.payhub.pdnd.anpr.c030.dto.RispostaOKDTO;

public interface AnprC030Client {
  RispostaOKDTO getIdAnprFromCf(RichiestaDTO clientAssertions);
}
