package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TipoListaSoggettiDTO {

    private List<TipoDatiSoggettiEnteDTO> datiSoggetto;
}
