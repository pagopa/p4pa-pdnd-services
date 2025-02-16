package it.gov.pagopa.payhub.pdnd.anpr.connector.c003;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.*;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c003.client.AnprC003Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnprC003ServiceTest {

    @Mock
    private AnprC003Client anprC003Client;

    @InjectMocks
    private AnprC003ServiceImpl anprC003Service;

    @Test
    void givenValidIdAnprAndFiscalCodeWhenGetUserDataThenReturnUserDataSuccessfully() {
        String fiscalCode = "DNTCRL65S67M126K";
        String idAnpr = "d20fcd8e-f228-323c-8924-6405b44879bf";

        TipoInfoSoggettoEnte subTypeInfo = TipoInfoSoggettoEnte.builder()
                .id("nome")
                .chiave("Jed")
                .valore(TipoInfoValore.S)
                .valoreTesto("Nome del soggetto")
                .valoreData("2024-11-02")
                .dettaglio("")
                .build();

        TipoDatiSoggettiEnte subDataTypes = TipoDatiSoggettiEnte.builder()
                .infoSoggettoEnte(List.of(subTypeInfo))
                .build();

        TipoListaSoggetti subTypeList = TipoListaSoggetti.builder()
                .datiSoggetto(List.of(subDataTypes))
                .build();

        RispostaE002OK mockResponse = RispostaE002OK.builder()
                .idOperazioneANPR("12345")
                .listaSoggetti(subTypeList)
                .listaAnomalie(null)
                .build();

        when(anprC003Client.getUserData(Mockito.any(RichiestaE002.class))).thenReturn(mockResponse);

        RispostaE002OK response = anprC003Service.getUserData(idAnpr, fiscalCode);

        assertNotNull(response);
        assertEquals("Jed", response.getListaSoggetti().getDatiSoggetto().getFirst().getInfoSoggettoEnte().getFirst().getChiave());
    }
}