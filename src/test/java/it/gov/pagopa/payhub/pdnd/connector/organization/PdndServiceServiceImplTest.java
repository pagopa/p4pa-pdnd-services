package it.gov.pagopa.payhub.pdnd.connector.organization;

import it.gov.pagopa.payhub.pdnd.connector.organization.client.PdndServiceSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
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
class PdndServiceServiceImplTest {
    @Mock
    private PdndServiceSearchClient pdndServiceSearchClientMock;

    private PdndServiceService pdndServiceService;

    @BeforeEach
    void setUp() {
        pdndServiceService = new PdndServiceServiceImpl(
                pdndServiceSearchClientMock
        );
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(
                pdndServiceSearchClientMock
        );
    }

    @Test
    void whenFindByClientIdAndServiceTypeThenOk() {
        // Given
        String accessToken = "accessToken";
        String clientId = "clientId";
        PdndServiceType pdndServiceType = PdndServiceType.SEND;

        PdndService expectedResult = new PdndService();

        Mockito.when(pdndServiceSearchClientMock.findByClientIdAndServiceType(clientId, pdndServiceType,accessToken))
                        .thenReturn(expectedResult);

        PdndService result = pdndServiceService.findByClientIdAndServiceType(clientId,pdndServiceType, accessToken);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }
}
