package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RispostaOKDTO {

    private String idOperazioneANPR;

    private TipoListaSoggettiDTO listaSoggetti;

    private List<TipoErroriAnomaliaDTO> listaAnomalie;

}
