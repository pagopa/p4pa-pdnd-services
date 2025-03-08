package it.gov.pagopa.payhub.pdnd.anpr.connector.c030;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoCriteriRicercaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoDatiRichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.client.AnprC030Client;
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
class AnprC030ServiceTest {

    @Mock
    private AnprC030Client anprC030ClientMock;
    @Mock
    private PdndServiceIntegratedConfig pdndServiceIntegratedConfig;
    @Mock
    private PdndService pdndServiceMock;
    @Mock
    private AnprIdOperationGeneratorService idOperationGeneratorServiceMock;

    private AnprC030ServiceImpl anprC030Service;

    @BeforeEach
    void init(){
        AnprApiClientConfig anprApiClientConfigMock = Mockito.mock(AnprApiClientConfig.class, Answers.RETURNS_DEEP_STUBS);
        when(anprApiClientConfigMock.getServices().getC030())
                .thenReturn(pdndServiceIntegratedConfig);

        anprC030Service = new AnprC030ServiceImpl(anprC030ClientMock, anprApiClientConfigMock, pdndServiceMock, idOperationGeneratorServiceMock);
    }

    @AfterEach
    void verifyNoMoreInteractions(){
        Mockito.verifyNoMoreInteractions(
                anprC030ClientMock,
                pdndServiceIntegratedConfig,
                pdndServiceMock,
                idOperationGeneratorServiceMock
        );
    }

    @Test
    void givenValidFiscalCodeWhenGetIdAnprFromFcThenReturnValidResponse() {
        String accessToken = "ACCESSTOKEN";
        String fiscalCode = "DNTCRL65S67M126K";
        String idOp = "IDOP";
        RispostaE002OK expectedResult = new RispostaE002OK();

        TipoCriteriRicercaE002 searchTypes = TipoCriteriRicercaE002.builder()
                .codiceFiscale(fiscalCode)
                .build();

        TipoDatiRichiestaE002 reqDataTypes = TipoDatiRichiestaE002.builder()
                .casoUso("C030")
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
        when(anprC030ClientMock.getIdAnprFromFc(request, accessToken))
                .thenReturn(expectedResult);
        when(idOperationGeneratorServiceMock.generateId())
                .thenReturn(idOp);
        when(anprC030ClientMock.getIdAnprFromFc(request, accessToken))
                .thenReturn(expectedResult);

        RispostaE002OK result = anprC030Service.getIdAnprFromFc(fiscalCode);

        assertSame(expectedResult, result);
    }
}