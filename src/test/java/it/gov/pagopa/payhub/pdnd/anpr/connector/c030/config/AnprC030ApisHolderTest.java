package it.gov.pagopa.payhub.pdnd.anpr.connector.c030.config;

import it.gov.pagopa.anpr.c030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.json.JsonConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedStaticConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.HttpsClientConfig;
import it.gov.pagopa.payhub.pdnd.connector.BasePdndServiceIntegratedApiHolderTest;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnprC030ApisHolderTest extends BasePdndServiceIntegratedApiHolderTest {

    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    private final PdndApiClientConfig pdndApiClientConfig = PdndApiClientConfig.builder()
            .config(PdndApiClientConfig.PdndConfig.builder()
                    .audience("PDNDAUDIENCE")
                    .authExpirationMinutes(100L)
                    .build())
            .maxAttempts(3)
            .build();
    private final AnprApiClientConfig anprApiClientConfig = AnprApiClientConfig.builder()
            .baseUrl("http://example.com")
            .maxAttempts(3)
            .https(new HttpsClientConfig())
            .services(AnprApiClientConfig.AnprServicesConfig.builder()
                    .c030(PdndServiceIntegratedStaticConfig.builder()
                            .basePath("anprC030BasePath")
                            .audience("anprC030Audience")
                            .build())
                    .build())
            .build();
    private final HttpClientConfig httpClientConfig = HttpClientConfig.builder()
            .connectionPool(new HttpClientConfig.HttpClientConnectionPoolConfig())
            .timeout(new HttpClientConfig.HttpClientTimeoutConfig())
            .build();

    private AnprC030ApisHolder apisHolder;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
        when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
        when(restTemplateMock.getInterceptors()).thenReturn(interceptors);

        apisHolder = new AnprC030ApisHolder(pdndApiClientConfig, anprApiClientConfig, restTemplateBuilderMock, httpClientConfig, new JsonConfig().objectMapperJackson3());

        verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getE002ServiceApi(mock(PdndAuthData.class)));
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
    void testRetryConfiguration() {
        assertRetry(anprApiClientConfig,
                pdndAuthData -> apisHolder.getE002ServiceApi(pdndAuthData)
                        .e002(new RichiestaE002()),
                new ParameterizedTypeReference<>() {}
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
