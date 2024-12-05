package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TipoComuneDTO {

    private String nomeComune;

    private String codiceIstat;

    private String siglaProvinciaIstat;

    private String descrizioneLocalita;

}
