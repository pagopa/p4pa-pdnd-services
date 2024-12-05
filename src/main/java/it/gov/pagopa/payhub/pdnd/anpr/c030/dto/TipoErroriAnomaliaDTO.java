package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TipoErroriAnomaliaDTO {

    private String codiceErroreAnomalia;

    private String tipoErroreAnomalia;

    private String testoErroreAnomalia;

    private String oggettoErroreAnomalia;

    private String campoErroreAnomalia;

    private String valoreErroreAnomalia;

}
