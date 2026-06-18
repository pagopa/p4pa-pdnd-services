package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.organization.PdndClientService;
import it.gov.pagopa.payhub.pdnd.connector.organization.PdndServiceService;
import it.gov.pagopa.payhub.pdnd.exception.custom.NotFoundException;
import it.gov.pagopa.payhub.pdnd.utils.ErrorCodeConstants;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdndIntegratedConfigResolverServiceTest {

    @Mock
    private PdndServiceService pdndServiceServiceMock;
    @Mock
    private PdndClientService pdndClientServiceMock;
    private static final String ANPR_C030_BASE_PATH = "anprC030BasePath";
    private static final String ANPR_C030_AUDIENCE = "anprC030Audience";
    private static final String ANPR_C003_BASE_PATH = "anprC003BasePath";
    private static final String ANPR_C003_AUDIENCE = "anprC003Audience";
    private static final String SEND_BASE_PATH = "sendBasePath";

    private PdndIntegratedConfigResolverService pdndIntegratedConfigResolverService;

    @BeforeEach
    void setUp() {
        pdndIntegratedConfigResolverService = new PdndIntegratedConfigResolverService(
                pdndServiceServiceMock, pdndClientServiceMock, ANPR_C030_BASE_PATH,
                ANPR_C030_AUDIENCE, ANPR_C003_BASE_PATH, ANPR_C003_AUDIENCE, SEND_BASE_PATH);
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(
                pdndServiceServiceMock,
                pdndClientServiceMock
        );
    }

    @ParameterizedTest
    @MethodSource("getIntegratedConfigSource")
    void whenGetIntegratedConfigThenExpectedValue(PdndServiceType service, String audience, String basePath) {
        // Given
        String accessToken = "accessToken";
        Long organizationId = 1L;
        String subUnitCode = "subUnitCode";
        PdndClientDTO pdndClientDTO = new PdndClientDTO();
        pdndClientDTO.setClientId("clientId");
        pdndClientDTO.setOrganizationId(organizationId);
        pdndClientDTO.setSubUnitCode(subUnitCode);
        pdndClientDTO.setClientName("clientName");
        pdndClientDTO.setKid("kid");
        pdndClientDTO.setPrivateKey("privateKey");
        pdndClientDTO.setPublicKey("publicKey");
        PdndService pdndService = new PdndService();
        pdndService.setPurposeId("purposeId");
        pdndService.setServiceName("serviceName");
        pdndService.setServiceType(service);
        pdndService.setClientId("clientId");

        PdndServiceIntegratedConfig expectedResult = new PdndServiceIntegratedConfig();
        expectedResult.setBasePath(basePath);
        expectedResult.setPurposeId(pdndService.getPurposeId());
        expectedResult.setAudience(audience);
        expectedResult.setClientId(pdndClientDTO.getClientId());
        expectedResult.setKid(pdndClientDTO.getKid());
        expectedResult.setPrivateKey(pdndClientDTO.getPrivateKey());
        expectedResult.setPublicKey(pdndClientDTO.getPublicKey());

        Mockito.when(pdndClientServiceMock.getPdndClientByOrganizationIdAndPdndServiceType(organizationId,service,subUnitCode,accessToken))
                        .thenReturn(pdndClientDTO);
        Mockito.when(pdndServiceServiceMock.findByClientIdAndServiceType(pdndClientDTO.getClientId(), service, accessToken))
                .thenReturn(pdndService);

        PdndServiceIntegratedConfig result = pdndIntegratedConfigResolverService.getIntegratedConfig(service,organizationId,subUnitCode,accessToken);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> getIntegratedConfigSource() {
        return Stream.of(
                Arguments.of(PdndServiceType.SEND, null, SEND_BASE_PATH),
                Arguments.of(PdndServiceType.C003, ANPR_C003_AUDIENCE, ANPR_C003_BASE_PATH),
                Arguments.of(PdndServiceType.C030, ANPR_C030_AUDIENCE, ANPR_C030_BASE_PATH)
        );
    }

    @Test
    void givenNoPdndServiceWhenGetIntegratedConfigThenNotFoundException() {
        // Given
        String accessToken = "accessToken";
        PdndServiceType service = PdndServiceType.SEND;
        Long organizationId = 1L;
        String subUnitCode = "subUnitCode";
        PdndClientDTO pdndClientDTO = new PdndClientDTO();

        Mockito.when(pdndClientServiceMock.getPdndClientByOrganizationIdAndPdndServiceType(organizationId,service,subUnitCode,accessToken))
                        .thenReturn(pdndClientDTO);
        Mockito.when(pdndServiceServiceMock.findByClientIdAndServiceType(pdndClientDTO.getClientId(), service, accessToken))
                .thenReturn(null);

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> pdndIntegratedConfigResolverService.getIntegratedConfig(service, organizationId, subUnitCode, accessToken));

        assertEquals(ErrorCodeConstants.ERROR_CODE_PDND_SERVICE,notFoundException.getCode());
    }

    @Test
    void givenNoPdndClientWhenGetIntegratedConfigThenNull() {
        // Given
        String accessToken = "accessToken";
        PdndServiceType service = PdndServiceType.SEND;
        Long organizationId = 1L;
        String subUnitCode = "subUnitCode";

        Mockito.when(pdndClientServiceMock.getPdndClientByOrganizationIdAndPdndServiceType(organizationId,service,subUnitCode,accessToken))
                        .thenReturn(null);

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> pdndIntegratedConfigResolverService.getIntegratedConfig(service, organizationId, subUnitCode, accessToken));

        assertEquals(ErrorCodeConstants.ERROR_CODE_PDND_CLIENT,notFoundException.getCode());
    }
}
