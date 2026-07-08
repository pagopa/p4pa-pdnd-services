package it.gov.pagopa.payhub.pdnd.connector.organization;

import it.gov.pagopa.payhub.pdnd.connector.organization.client.PdndClientClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PdndClientServiceImplTest {
    @Mock
    private PdndClientClient pdndClientClientMock;

    private PdndClientService pdndClientService;

    @BeforeEach
    void setUp() {
        pdndClientService = new PdndClientServiceImpl(
                pdndClientClientMock
        );
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(
                pdndClientClientMock
        );
    }

    @Test
    void whenGetUsablePdndClientByOrganizationIdAndPdndServiceTypeThenExpectedValue() {
        // Given
        String accessToken = "accessToken";
        Long organizationId = 1L;
        PdndServiceType service = PdndServiceType.SEND;
        String subUnitCode = "subUnitCode";

        PdndClientDTO expectedResult = new PdndClientDTO();

        Mockito.when(pdndClientClientMock.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId,service,subUnitCode,accessToken))
                        .thenReturn(expectedResult);

        PdndClientDTO result = pdndClientService.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId, service,subUnitCode,accessToken);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }
}
