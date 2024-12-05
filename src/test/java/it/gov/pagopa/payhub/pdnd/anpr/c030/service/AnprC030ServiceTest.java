package it.gov.pagopa.payhub.pdnd.anpr.c030.service;

import it.gov.pagopa.payhub.pdnd.anpr.c030.client.AnprC030ClientImpl;
import it.gov.pagopa.payhub.pdnd.anpr.c030.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnprC030ServiceTest {

    @Mock
    private AnprC030ClientImpl anprC030ClientImpl;

    @InjectMocks
    private AnprC030Service anprC030Service;

    @Test
    void testGetIdAnprFromCf() {
        String fiscalCode = "DNTCRL65S67M126K";

        TipoIdentificativiDTO tipoIdentificativiDTO = TipoIdentificativiDTO.builder()
                .idANPR("d20fcd8e-f228-323c-8924-6405b44879bf").build();

        TipoDatiSoggettiEnteDTO tipoDatiSoggettiEnteDTO = TipoDatiSoggettiEnteDTO.builder()
                .identificativi(tipoIdentificativiDTO).build();

        TipoListaSoggettiDTO tipoListaSoggettiDTO = TipoListaSoggettiDTO.builder()
                .datiSoggetto(List.of(tipoDatiSoggettiEnteDTO)).build();

        RispostaOKDTO mockResponse = RispostaOKDTO.builder()
                .idOperazioneANPR("12345")
                .listaSoggetti(tipoListaSoggettiDTO)
                .listaAnomalie(null)
                .build();

        when(anprC030ClientImpl.getIdAnprFromCf(Mockito.any(RichiestaDTO.class))).thenReturn(mockResponse);

        RispostaOKDTO response = anprC030Service.getIdAnprFromCf(fiscalCode);

        assertNotNull(response);
        assertEquals("d20fcd8e-f228-323c-8924-6405b44879bf", response.getListaSoggetti().getDatiSoggetto().getFirst().getIdentificativi().getIdANPR());
    }
}