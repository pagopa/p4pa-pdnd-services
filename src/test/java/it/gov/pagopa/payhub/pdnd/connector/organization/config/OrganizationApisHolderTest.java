package it.gov.pagopa.payhub.pdnd.connector.organization.config;

import it.gov.pagopa.payhub.pdnd.config.json.JsonConfig;
import it.gov.pagopa.payhub.pdnd.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationApisHolderTest extends BaseApiHolderTest {

    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    private OrganizationApisHolder apisHolder;
    private OrganizationApiClientConfig apiClientConfig;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
        when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

        apiClientConfig = OrganizationApiClientConfig.builder()
                .baseUrl("http://example.com")
                .maxAttempts(3)
                .build();
        apisHolder = new OrganizationApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

        verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getPdndClientApi(null));
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
                accessToken -> apisHolder.getPdndClientApi(accessToken)
                        .getUsablePdndClientByOrganizationIdAndPdndServiceType(1L, PdndServiceType.SEND,"subUnitCode"),
                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    void whenGetPdndClientApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
        assertAuthenticationShouldBeSetInThreadSafeMode(
                accessToken -> apisHolder.getPdndClientApi(accessToken)
                        .getUsablePdndClientByOrganizationIdAndPdndServiceType(1L, PdndServiceType.SEND,"subUnitCode"),
                new ParameterizedTypeReference<>() {},
                apisHolder::unload);
    }

    @Test
    void whenGetPdndServiceSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
        assertAuthenticationShouldBeSetInThreadSafeMode(
                accessToken -> apisHolder.getPdndServiceSearchControllerApi(accessToken)
                        .crudPdndServicesFindByClientIdAndServiceType("clientId", PdndServiceType.SEND),
                new ParameterizedTypeReference<>() {},
                apisHolder::unload);
    }
}

