package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TipoLocalitaDTO {

    private String descrizioneLocalita;

    private String descrizioneStato;

    private String codiceStato;

    private String provinciaContea;

}
