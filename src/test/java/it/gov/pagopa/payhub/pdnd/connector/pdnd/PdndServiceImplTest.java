package it.gov.pagopa.payhub.pdnd.connector.pdnd;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndAuthDataBuilderService;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndIntegratedConfigResolverService;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class PdndServiceImplTest {

    @Mock
    private PdndAuthDataBuilderService pdndAuthDataBuilderServiceMock;
    @Mock
    private PdndIntegratedConfigResolverService pdndIntegratedConfigResolverServiceMock;

    private PdndServiceImpl pdndService;

    @BeforeEach
    void setUp() {
        pdndService = new PdndServiceImpl(pdndAuthDataBuilderServiceMock, pdndIntegratedConfigResolverServiceMock);
    }

    @Test
    void givenValidConfigWhenGenerateTokenThenGeneratesNewToken() {
        // Given
        String accessToken = "accessToken";
        PdndServiceType service = PdndServiceType.SEND;
        Long organizationId = 1L;
        String subUnitCode = "subUnitCode";
        PdndServiceIntegratedConfig serviceConfig = new PdndServiceIntegratedConfig();
        PdndAuthData expectedResult = Mockito.mock(PdndAuthData.class);

        // When
        Mockito.when(pdndIntegratedConfigResolverServiceMock.getIntegratedConfig(service,organizationId,subUnitCode,accessToken))
                        .thenReturn(serviceConfig);
        Mockito.when(pdndAuthDataBuilderServiceMock.build(Mockito.same(serviceConfig)))
                .thenReturn(expectedResult);

        PdndAuthData result = pdndService.generateToken(service,organizationId,subUnitCode,accessToken);

        // Then
        assertSame(expectedResult, result);
    }

    @Test
    void givenTokenInCacheWhenGenerateTokenThenReturnCachedToken() {
        // Given
        String accessToken = "accessToken";
        PdndServiceType service = PdndServiceType.SEND;
        Long organizationId = 1L;
        String subUnitCode = "subUnitCode";
        LocalDateTime localDateTime = LocalDateTime.of(2026, 6, 18, 12, 0);
        PdndAuthData expectedResult = new PdndAuthData(null, null, "TOKEN", LocalDateTime.of(2026,6,18,12,1), "clientId", "audience", "kid", "basePath", null);
        pdndService.jwtCache.put(Triple.of(service,organizationId,subUnitCode), expectedResult);

        try(MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class)) {
            localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTime);

            PdndAuthData result = pdndService.generateToken(service,organizationId,subUnitCode,accessToken);

            // Then
            assertSame(expectedResult, result);
        }
    }

}