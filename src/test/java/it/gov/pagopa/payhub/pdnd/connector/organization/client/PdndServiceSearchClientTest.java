package it.gov.pagopa.payhub.pdnd.connector.organization.client;

import it.gov.pagopa.payhub.pdnd.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.controller.generated.PdndServiceSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

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
  void whenFindByClientIdThenOk() {
    // Given
    String accessToken = "ACCESS_TOKEN";
    String clientId = "clientId";
    PdndService expectedResult = new PdndService();

    Mockito.when(organizationApisHolderMock.getPdndServiceSearchControllerApi(accessToken))
            .thenReturn(pdndServiceSearchControllerApiMock);
    Mockito.when(pdndServiceSearchControllerApiMock.crudPdndServicesFindByClientId(clientId))
            .thenReturn(expectedResult);

    // When
    PdndService result = pdndServiceSearchClient.findByClientId(clientId, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNotFoundWhenFindByClientIdThenNull() {
    // Given
    String accessToken = "ACCESS_TOKEN";
    String clientId = "clientId";

    Mockito.when(organizationApisHolderMock.getPdndServiceSearchControllerApi(accessToken))
            .thenReturn(pdndServiceSearchControllerApiMock);
    Mockito.when(pdndServiceSearchControllerApiMock.crudPdndServicesFindByClientId(clientId))
            .thenThrow(
                    HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    // When
    PdndService result = pdndServiceSearchClient.findByClientId(clientId, accessToken);

    // Then
    Assertions.assertNull(result);
  }
}