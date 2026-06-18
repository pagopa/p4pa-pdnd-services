package it.gov.pagopa.payhub.pdnd.anpr.connector.c003.config;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.HttpsClientConfig;
import it.gov.pagopa.payhub.pdnd.connector.BasePdndServiceIntegratedApiHolderTest;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class AnprC003ApisHolderTest extends BasePdndServiceIntegratedApiHolderTest {
    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    private final PdndApiClientConfig pdndApiClientConfig = PdndApiClientConfig.builder()
            .config(PdndApiClientConfig.PdndConfig.builder()
                    .authExpirationMinutes(100L)
                    .audience("PDNDAUDIENCE")
                    .build())
            .build();
    private final AnprApiClientConfig pdndServiceIntegratedApiClientConfig = AnprApiClientConfig.builder()
            .baseUrl("http://example.com")
            .https(HttpsClientConfig.builder()
                    .trustAll(true)
                    .build())
            .build();
    private final HttpClientConfig httpClientConfig = HttpClientConfig.builder()
            .connectionPool(new HttpClientConfig.HttpClientConnectionPoolConfig())
            .timeout(new HttpClientConfig.HttpClientTimeoutConfig())
            .build();
    private final String basePath = "basePath";

    private AnprC003ApisHolder apisHolder;

    @BeforeEach
    void setUp() {
        Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
        Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
        Mockito.when(restTemplateMock.getInterceptors()).thenReturn(interceptors);
        Mockito.doNothing()
                .when(restTemplateMock)
                .setRequestFactory(Mockito.any());

        apisHolder = new AnprC003ApisHolder(pdndApiClientConfig, pdndServiceIntegratedApiClientConfig, restTemplateBuilderMock, httpClientConfig, basePath);
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(
                restTemplateBuilderMock,
                restTemplateMock,
                jwsSignerMock
        );
    }

    @Test
    void whenGetE002ServiceApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException, IOException, IllegalAccessException {
        assertAuthenticationShouldBeSetInThreadSafeMode(
                pdndApiClientConfig.getConfig(),
                pdndAuthData -> apisHolder.getE002ServiceApi(pdndAuthData)
                        .e002(new RichiestaE002()),
                new ParameterizedTypeReference<>() {},
                apisHolder::unload);
    }

}
