package it.gov.pagopa.payhub.pdnd.anpr.connector.c030.client;

import it.gov.pagopa.payhub.anpr.C030.controller.generated.E002ServiceApi;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.config.AnprC030ApisHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
        String accessToken = "ACCESSTOKEN";
        RichiestaE002 request = new RichiestaE002();
        RispostaE002OK expectedResult = new RispostaE002OK();

        Mockito.when(apisHolder.getE002ServiceApi(accessToken))
                .thenReturn(e002ServiceApiMock);
        Mockito.when(e002ServiceApiMock.e002(request))
                .thenReturn(expectedResult);

        // When
        RispostaE002OK result = client.getIdAnprFromFc(request, accessToken);

        // Then
        Assertions.assertSame(expectedResult, result);
    }

}
