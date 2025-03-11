package it.gov.pagopa.payhub.pdnd.anpr.connector.c030.config;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.HttpClientConfig;
import it.gov.pagopa.payhub.pdnd.config.HttpsClientConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.BasePdndServiceIntegratedApiHolderTest;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class AnprC030ApisHolderTest extends BasePdndServiceIntegratedApiHolderTest {
    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    private final PdndApiClientConfig pdndApiClientConfig = PdndApiClientConfig.builder()
            .config(PdndApiClientConfig.PdndConfig.builder()
                    .audience("PDNDAUDIENCE")
                    .authExpirationMinutes(100L)
                    .build())
            .build();
    private final AnprApiClientConfig pdndServiceIntegratedApiClientConfig = AnprApiClientConfig.builder()
            .baseUrl("http://example.com")
            .https(new HttpsClientConfig())
            .services(AnprApiClientConfig.AnprServicesConfig.builder()
                    .c030(new PdndServiceIntegratedConfig())
                    .build())
            .build();
    private final HttpClientConfig httpClientConfig = HttpClientConfig.builder()
            .connectionPool(new HttpClientConfig.HttpClientConnectionPoolConfig())
            .timeout(new HttpClientConfig.HttpClientTimeoutConfig())
            .build();

    private AnprC030ApisHolder apisHolder;

    @BeforeEach
    void setUp() {
        Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
        Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
        Mockito.when(restTemplateMock.getInterceptors()).thenReturn(interceptors);

        apisHolder = new AnprC030ApisHolder(pdndApiClientConfig, pdndServiceIntegratedApiClientConfig, restTemplateBuilderMock, httpClientConfig);
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
                pdndServiceIntegratedApiClientConfig.getServices().getC030(),
                pdndAuthData -> apisHolder.getE002ServiceApi(pdndAuthData)
                        .e002(new RichiestaE002()),
                new ParameterizedTypeReference<>() {},
                apisHolder::unload);
    }

}
