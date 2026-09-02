package it.gov.pagopa.payhub.pdnd.connector.organization.client;

import it.gov.pagopa.payhub.pdnd.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.payhub.pdnd.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.client.generated.PdndServiceSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndServiceSearchClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private PdndServiceSearchControllerApi pdndServiceSearchControllerApiMock;

  private PdndServiceSearchClient pdndServiceSearchClient;

  @BeforeEach
  void setUp() {
    pdndServiceSearchClient = new PdndServiceSearchClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
            organizationApisHolderMock,
            pdndServiceSearchControllerApiMock
    );
  }

  @Test
  void whenFindByClientIdAndServiceTypeThenOk() {
    // Given
    String accessToken = "ACCESS_TOKEN";
    String clientId = "clientId";
    PdndServiceType pdndServiceType = PdndServiceType.SEND;
    PdndService expectedResult = new PdndService();

    when(organizationApisHolderMock.getPdndServiceSearchControllerApi(accessToken))
            .thenReturn(pdndServiceSearchControllerApiMock);
    when(pdndServiceSearchControllerApiMock.crudPdndServicesFindByClientIdAndServiceType(clientId, pdndServiceType))
            .thenReturn(expectedResult);

    // When
    PdndService result = pdndServiceSearchClient.findByClientIdAndServiceType(clientId, pdndServiceType, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNotFoundWhenFindByClientIdAndServiceTypeThenNull() {
    // Given
    String accessToken = "ACCESS_TOKEN";
    String clientId = "clientId";
    PdndServiceType pdndServiceType = PdndServiceType.SEND;

    when(organizationApisHolderMock.getPdndServiceSearchControllerApi(accessToken))
            .thenReturn(pdndServiceSearchControllerApiMock);
    when(pdndServiceSearchControllerApiMock.crudPdndServicesFindByClientIdAndServiceType(clientId, pdndServiceType))
            .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    // When
    PdndService result = pdndServiceSearchClient.findByClientIdAndServiceType(clientId, pdndServiceType, accessToken);

    // Then
    Assertions.assertNull(result);
  }
}