package it.gov.pagopa.payhub.pdnd.connector.pdnd;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndAuthDataBuilderService;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndServiceIntegratedConfigResolverService;
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
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class PdndServiceImplTest {

    @Mock
    private PdndAuthDataBuilderService pdndAuthDataBuilderServiceMock;
    @Mock
    private PdndServiceIntegratedConfigResolverService pdndServiceIntegratedConfigResolverServiceMock;

    private PdndServiceImpl pdndService;

    @BeforeEach
    void setUp() {
        pdndService = new PdndServiceImpl(pdndAuthDataBuilderServiceMock, pdndServiceIntegratedConfigResolverServiceMock);
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
        Mockito.when(pdndServiceIntegratedConfigResolverServiceMock.getPdndServiceIntegratedConfig(service,organizationId,subUnitCode,accessToken))
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
        LocalDateTime localDateTime = LocalDateTime.of(2026, Month.JUNE, 18, 12, 0);
        PdndServiceIntegratedConfig pdndServiceIntegratedConfig = PdndServiceIntegratedConfig.builder()
                .clientId("clientId")
                .audience("serviceAudience")
                .kid("kid")
                .build();
        PdndAuthData expectedResult = new PdndAuthData(null, null, "TOKEN", LocalDateTime.of(2026,Month.JUNE,18,12,1), pdndServiceIntegratedConfig, pdndServiceIntegratedConfig.getAudience(), null);
        pdndService.jwtCache.put(Triple.of(service,organizationId,subUnitCode), expectedResult);

        try(MockedStatic<LocalDateTime> localDateTimeMock = Mockito.mockStatic(LocalDateTime.class)) {
            localDateTimeMock.when(LocalDateTime::now).thenReturn(localDateTime);

            PdndAuthData result = pdndService.generateToken(service,organizationId,subUnitCode,accessToken);

            // Then
            assertSame(expectedResult, result);
        }
    }

}