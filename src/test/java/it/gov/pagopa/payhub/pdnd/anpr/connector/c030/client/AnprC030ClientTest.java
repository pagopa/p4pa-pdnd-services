package it.gov.pagopa.payhub.pdnd.anpr.connector.c030.client;

import it.gov.pagopa.anpr.c030.client.generated.E002ServiceApi;
import it.gov.pagopa.anpr.c030.dto.generated.RichiestaE002;
import it.gov.pagopa.anpr.c030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.config.AnprC030ApisHolder;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnprC030ClientTest {
    @Mock
    private AnprC030ApisHolder apisHolder;
    @Mock
    private E002ServiceApi e002ServiceApiMock;

    private AnprC030Client client;

    @BeforeEach
    void setUp() {
        client = new AnprC030Client(apisHolder);
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(
                apisHolder,
                e002ServiceApiMock
        );
    }

    @Test
    void whenGetIdAnprFromFcThenInvokeWithAccessToken() {
        // Given
        PdndAuthData pdndAuthData = mock(PdndAuthData.class);
        RichiestaE002 request = new RichiestaE002();
        RispostaE002OK expectedResult = new RispostaE002OK();

        when(apisHolder.getE002ServiceApi(Mockito.same(pdndAuthData)))
                .thenReturn(e002ServiceApiMock);
        when(e002ServiceApiMock.e002(request))
                .thenReturn(expectedResult);

        // When
        RispostaE002OK result = client.getIdAnprFromFc(request, pdndAuthData);

        // Then
        Assertions.assertSame(expectedResult, result);
    }

}
