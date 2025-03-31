package it.gov.pagopa.payhub.pdnd.anpr.connector.c003;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoCriteriRicercaE002;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoDatiRichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c003.client.AnprC003Client;
import it.gov.pagopa.payhub.pdnd.anpr.service.AnprIdOperationGeneratorService;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.PdndService;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnprC003ServiceTest {

    @Mock
    private AnprC003Client anprC003ClientMock;
    @Mock
    private PdndServiceIntegratedConfig pdndServiceIntegratedConfig;
    @Mock
    private PdndService pdndServiceMock;
    @Mock
    private AnprIdOperationGeneratorService idOperationGeneratorServiceMock;

    private AnprC003ServiceImpl anprC003Service;

    @BeforeEach
    void init(){
        AnprApiClientConfig anprApiClientConfigMock = Mockito.mock(AnprApiClientConfig.class, Answers.RETURNS_DEEP_STUBS);
        when(anprApiClientConfigMock.getServices().getC003())
                .thenReturn(pdndServiceIntegratedConfig);

        anprC003Service = new AnprC003ServiceImpl(anprC003ClientMock, anprApiClientConfigMock, pdndServiceMock, idOperationGeneratorServiceMock);
    }

    @AfterEach
    void verifyNoMoreInteractions(){
        Mockito.verifyNoMoreInteractions(
                anprC003ClientMock,
                pdndServiceIntegratedConfig,
                pdndServiceMock,
                idOperationGeneratorServiceMock
        );
    }

    @Test
    void givenValidIdAnprAndFiscalCodeWhenGetUserDataThenReturnUserDataSuccessfully() {
        String accessToken = "ACCESSTOKEN";
        String idAnpr = "d20fcd8e-f228-323c-8924-6405b44879bf";
        String idOp = "IDOP";
        RispostaE002OK expectedResult = new RispostaE002OK();

        TipoCriteriRicercaE002 searchTypes = TipoCriteriRicercaE002.builder()
                .idANPR(idAnpr)
                .build();

        TipoDatiRichiestaE002 reqDataTypes = TipoDatiRichiestaE002.builder()
                .casoUso("C003")
                .dataRiferimentoRichiesta(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .motivoRichiesta("1")
                .build();

        RichiestaE002 request = RichiestaE002.builder()
                .idOperazioneClient(idOp)
                .criteriRicerca(searchTypes)
                .datiRichiesta(reqDataTypes)
                .build();

        PdndAuthData pdndAuthData = new PdndAuthData(null, null, accessToken, null, null);

        when(pdndServiceMock.generateToken(Mockito.same(pdndServiceIntegratedConfig)))
                .thenReturn(pdndAuthData);
        when(idOperationGeneratorServiceMock.generateId())
                .thenReturn(idOp);
        when(anprC003ClientMock.getUserData(request, pdndAuthData))
                .thenReturn(expectedResult);

        RispostaE002OK result = anprC003Service.getUserData(idAnpr);

        assertSame(expectedResult, result);
    }
}