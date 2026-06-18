package it.gov.pagopa.payhub.pdnd.anpr.connector.c030;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoCriteriRicercaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoDatiRichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.client.AnprC030Client;
import it.gov.pagopa.payhub.pdnd.anpr.service.AnprIdOperationGeneratorService;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.PdndService;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private PdndService pdndServiceMock;
    @Mock
    private AnprIdOperationGeneratorService idOperationGeneratorServiceMock;

    private AnprC030ServiceImpl anprC030Service;

    @BeforeEach
    void init(){

        anprC030Service = new AnprC030ServiceImpl(anprC030ClientMock, pdndServiceMock, idOperationGeneratorServiceMock);
    }

    @AfterEach
    void verifyNoMoreInteractions(){
        Mockito.verifyNoMoreInteractions(
                anprC030ClientMock,
                pdndServiceMock,
                idOperationGeneratorServiceMock
        );
    }

    @Test
    void givenValidFiscalCodeWhenGetIdAnprFromFcThenReturnValidResponse() {
        String accessToken = "ACCESSTOKEN";
        String fiscalCode = "DNTCRL65S67M126K";
        String idOp = "IDOP";
        Long organizationId = 1L;
        String subUnitCode = "subUnitCode";
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

        PdndAuthData pdndAuthData = new PdndAuthData(null, null, accessToken, null, "clientId", "audience", "kid", "basePath", null);

        when(pdndServiceMock.generateToken(PdndServiceType.C030,organizationId,subUnitCode, accessToken))
                .thenReturn(pdndAuthData);
        when(anprC030ClientMock.getIdAnprFromFc(request, pdndAuthData))
                .thenReturn(expectedResult);
        when(idOperationGeneratorServiceMock.generateId())
                .thenReturn(idOp);

        RispostaE002OK result = anprC030Service.getIdAnprFromFc(fiscalCode, organizationId, subUnitCode, accessToken);

        assertSame(expectedResult, result);
    }
}