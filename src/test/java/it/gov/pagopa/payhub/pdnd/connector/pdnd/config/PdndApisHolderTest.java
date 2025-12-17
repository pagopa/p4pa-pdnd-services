package it.gov.pagopa.payhub.pdnd.connector.pdnd.config;

import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.api.AuthApi;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@ExtendWith(MockitoExtension.class)
class PdndApisHolderTest {

    @Mock
    protected RestTemplate restTemplateMock;
    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    private PdndApisHolder pdndApisHolder;

    @BeforeEach
    void setUp() {
        Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
        Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
        PdndApiClientConfig clientConfig = PdndApiClientConfig.builder()
                .baseUrl("http://example.com")
                .build();
        pdndApisHolder = new PdndApisHolder(clientConfig, restTemplateBuilderMock);
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(
                restTemplateBuilderMock,
                restTemplateMock
        );
    }

    @Test
    void whenGetBrokerEntityControllerApi() {
        // Given
        ResponseEntity<Object> expectedResult = new ResponseEntity<>(HttpStatus.OK);
        Mockito.when(restTemplateMock.exchange(Mockito.any(), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(expectedResult);
        // When
        AuthApi auhtApi = pdndApisHolder.getBrokerEntityControllerApi();
        ResponseEntity<Void> result = auhtApi.invokeAPI("http://example.com", HttpMethod.GET);

        // Then
        Assertions.assertSame(expectedResult, result);
    }
}
