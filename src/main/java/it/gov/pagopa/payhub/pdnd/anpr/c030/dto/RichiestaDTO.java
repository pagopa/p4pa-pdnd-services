package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RichiestaDTO {

    private String idOperazioneClient;

    private TipoCriteriRicercaDTO criteriRicerca;

    private TipoDatiRichiestaDTO datiRichiesta;

}
