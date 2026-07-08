package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedStaticConfig;
import it.gov.pagopa.payhub.pdnd.connector.organization.PdndClientService;
import it.gov.pagopa.payhub.pdnd.connector.organization.PdndServiceService;
import it.gov.pagopa.payhub.pdnd.exception.custom.NotFoundException;
import it.gov.pagopa.payhub.pdnd.service.ApiClientConfigResolverService;
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
class PdndServiceIntegratedConfigResolverServiceTest {

    @Mock
    private PdndServiceService pdndServiceServiceMock;
    @Mock
    private PdndClientService pdndClientServiceMock;
    @Mock
    private ApiClientConfigResolverService apiClientConfigResolverServiceMock;

    private PdndServiceIntegratedConfigResolverService pdndServiceIntegratedConfigResolverService;

    @BeforeEach
    void setUp() {
        pdndServiceIntegratedConfigResolverService = new PdndServiceIntegratedConfigResolverService(
                pdndServiceServiceMock, pdndClientServiceMock, apiClientConfigResolverServiceMock);
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(
                pdndServiceServiceMock,
                pdndClientServiceMock
        );
    }

    @ParameterizedTest
    @MethodSource("getPdndServiceIntegratedConfigSource")
    void whenGetPdndServiceIntegratedConfigThenExpectedValue(PdndServiceType service, PdndServiceIntegratedStaticConfig config) {
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
        expectedResult.setBasePath(config.getBasePath());
        expectedResult.setPurposeId(pdndService.getPurposeId());
        expectedResult.setAudience(config.getAudience());
        expectedResult.setClientId(pdndClientDTO.getClientId());
        expectedResult.setKid(pdndClientDTO.getKid());
        expectedResult.setPrivateKey(pdndClientDTO.getPrivateKey());
        expectedResult.setPublicKey(pdndClientDTO.getPublicKey());

        Mockito.when(apiClientConfigResolverServiceMock.getIntegratedConfig(service)).thenReturn(config);
        Mockito.when(pdndClientServiceMock.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId,service,subUnitCode,accessToken))
                        .thenReturn(pdndClientDTO);
        Mockito.when(pdndServiceServiceMock.findByClientIdAndServiceType(pdndClientDTO.getClientId(), service, accessToken))
                .thenReturn(pdndService);

        PdndServiceIntegratedConfig result = pdndServiceIntegratedConfigResolverService.getPdndServiceIntegratedConfig(service,organizationId,subUnitCode,accessToken);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> getPdndServiceIntegratedConfigSource() {
        return Stream.of(
                Arguments.of(PdndServiceType.SEND, new PdndServiceIntegratedStaticConfig("sendBasePath",null)),
                Arguments.of(PdndServiceType.ANPR_C003, new PdndServiceIntegratedStaticConfig("anprC003BasePAth","anprC003Audience")),
                Arguments.of(PdndServiceType.ANPR_C030, new PdndServiceIntegratedStaticConfig("anprC030BasePAth","anprC030Audience"))
        );
    }

    @Test
    void givenNoPdndServiceWhenGetPdndServiceIntegratedConfigThenNotFoundException() {
        // Given
        String accessToken = "accessToken";
        PdndServiceType service = PdndServiceType.SEND;
        Long organizationId = 1L;
        String subUnitCode = "subUnitCode";
        PdndClientDTO pdndClientDTO = new PdndClientDTO();

        Mockito.when(apiClientConfigResolverServiceMock.getIntegratedConfig(service)).thenReturn(new PdndServiceIntegratedStaticConfig());
        Mockito.when(pdndClientServiceMock.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId,service,subUnitCode,accessToken))
                        .thenReturn(pdndClientDTO);
        Mockito.when(pdndServiceServiceMock.findByClientIdAndServiceType(pdndClientDTO.getClientId(), service, accessToken))
                .thenReturn(null);

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> pdndServiceIntegratedConfigResolverService.getPdndServiceIntegratedConfig(service, organizationId, subUnitCode, accessToken));

        assertEquals(ErrorCodeConstants.ERROR_CODE_PDND_SERVICE,notFoundException.getCode());
    }

    @Test
    void givenNoPdndClientWhenGetPdndServiceIntegratedConfigThenNull() {
        // Given
        String accessToken = "accessToken";
        PdndServiceType service = PdndServiceType.SEND;
        Long organizationId = 1L;
        String subUnitCode = "subUnitCode";

        Mockito.when(apiClientConfigResolverServiceMock.getIntegratedConfig(service)).thenReturn(new PdndServiceIntegratedStaticConfig());
        Mockito.when(pdndClientServiceMock.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId,service,subUnitCode,accessToken))
                        .thenReturn(null);

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> pdndServiceIntegratedConfigResolverService.getPdndServiceIntegratedConfig(service, organizationId, subUnitCode, accessToken));

        assertEquals(ErrorCodeConstants.ERROR_CODE_PDND_CLIENT,notFoundException.getCode());
    }
}
