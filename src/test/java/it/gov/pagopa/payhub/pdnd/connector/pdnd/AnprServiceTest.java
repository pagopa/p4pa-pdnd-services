package it.gov.pagopa.payhub.pdnd.connector.pdnd;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndAuthDataBuilderService;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class AnprServiceTest {

    @Mock
    private PdndAuthDataBuilderService pdndAuthDataBuilderServiceMock;

    private PdndServiceImpl pdndService;

    @BeforeEach
    void setUp() {
        pdndService = new PdndServiceImpl(pdndAuthDataBuilderServiceMock);
    }

    @Test
    void givenValidConfigWhenGenerateTokenThenGeneratesNewToken() {
        // Given
        PdndServiceIntegratedConfig serviceConfig = new PdndServiceIntegratedConfig();
        PdndAuthData expectedResult = Mockito.mock(PdndAuthData.class);

        // When
        Mockito.when(pdndAuthDataBuilderServiceMock.build(Mockito.same(serviceConfig)))
                .thenReturn(expectedResult);

        PdndAuthData result = pdndService.generateToken(serviceConfig);

        // Then
        assertSame(expectedResult, result);
    }

    @Test
    void givenTokenInCacheWhenGenerateTokenThenReturnCachedToken() {
        // Given
        PdndServiceIntegratedConfig serviceConfig = Mockito.mock(PdndServiceIntegratedConfig.class);
        PdndAuthData expectedResult = new PdndAuthData(null, null, "TOKEN", LocalDateTime.now().plusMinutes(1), null);
        pdndService.jwtCache.put(serviceConfig, expectedResult);

        PdndAuthData result = pdndService.generateToken(serviceConfig);

        // Then
        assertSame(expectedResult, result);
    }

}