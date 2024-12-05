package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TipoLuogoNascitaDTO {

    private String luogoEccezionale;

    private TipoComuneDTO comune;

    private TipoLocalitaDTO localita;

}
