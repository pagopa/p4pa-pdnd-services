package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TipoDatiRichiestaDTO {

    private String dataRiferimentoRichiesta;

    private String motivoRichiesta;

    private String casoUso;

}
