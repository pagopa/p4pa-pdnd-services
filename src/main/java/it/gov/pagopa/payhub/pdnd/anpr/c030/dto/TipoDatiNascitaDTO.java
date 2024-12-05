package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TipoDatiNascitaDTO {

    private String dataEvento;

    private String senzaGiorno;

    private String senzaGiornoMese;

    private TipoLuogoNascitaDTO luogoNascita;

}
