package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TipoDatiSoggettiEnteDTO {

    private TipoIdentificativiDTO identificativi;
}
