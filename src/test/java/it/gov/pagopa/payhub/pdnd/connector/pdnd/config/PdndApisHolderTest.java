package it.gov.pagopa.payhub.pdnd.connector.pdnd.config;

import it.gov.pagopa.payhub.pdnd.config.json.JsonConfig;
import it.gov.pagopa.payhub.pdnd.connector.BaseApiHolderTest;
import it.gov.pagopa.pdnd.client.generated.AuthApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndApisHolderTest extends BaseApiHolderTest {

    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    private PdndApisHolder apisHolder;
    private PdndApiClientConfig apiClientConfig;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
        when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

        apiClientConfig = PdndApiClientConfig.builder()
                .baseUrl("http://example.com")
                .maxAttempts(3)
                .build();
        apisHolder = new PdndApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

        verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getAuthApi());
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(
                restTemplateBuilderMock,
                restTemplateMock
        );
    }

    @Test
    void testRetryConfiguration() {
        assertRetry(apiClientConfig,
                accessToken -> apisHolder.getAuthApi()
                        .createToken("assertion", "assertionType", "grantType", "clientId"),
                new ParameterizedTypeReference<>() {}
        );
    }

    // PDND is invoked without Authorization header, thus we are not invoking the test to check its configuration thread safe as done on other apiHolder tests
    @Test
    void whenGetBrokerEntityControllerApi() {
        // Given
        ResponseEntity<Object> expectedResult = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplateMock.exchange(Mockito.any(), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(expectedResult);
        // When
        AuthApi authApi = apisHolder.getAuthApi();
        ResponseEntity<Void> result = authApi.invokeAPI("http://example.com", HttpMethod.GET);

        // Then
        Assertions.assertSame(expectedResult, result);
    }

}
