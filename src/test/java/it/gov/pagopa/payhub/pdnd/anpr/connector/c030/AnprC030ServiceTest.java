package it.gov.pagopa.payhub.pdnd.anpr.connector.c030;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.*;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.client.AnprC030Client;
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
    private AnprC030Client anprC030Client;

    @InjectMocks
    private AnprC030ServiceImpl anprC030Service;

    @Test
    void givenValidFiscalCodeWhenGetIdAnprFromFcThenReturnValidResponse() {
        String fiscalCode = "DNTCRL65S67M126K";

        TipoIdentificativi idTypes = TipoIdentificativi.builder()
                .idANPR("d20fcd8e-f228-323c-8924-6405b44879bf").build();

        TipoDatiSoggettiEnte subDataTypes = TipoDatiSoggettiEnte.builder()
                .identificativi(idTypes).build();

        TipoListaSoggetti subListTypes = TipoListaSoggetti.builder()
                .datiSoggetto(List.of(subDataTypes)).build();

        RispostaE002OK mockResponse = RispostaE002OK.builder()
                .idOperazioneANPR("12345")
                .listaSoggetti(subListTypes)
                .listaAnomalie(null)
                .build();

        when(anprC030Client.getIdAnprFromFc(Mockito.any(RichiestaE002.class))).thenReturn(mockResponse);

        RispostaE002OK response = anprC030Service.getIdAnprFromFc(fiscalCode);

        assertNotNull(response);
        assertEquals("d20fcd8e-f228-323c-8924-6405b44879bf", response.getListaSoggetti().getDatiSoggetto().getFirst().getIdentificativi().getIdANPR());
    }
}