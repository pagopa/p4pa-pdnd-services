package it.gov.pagopa.payhub.pdnd.connector.organization.client;

import it.gov.pagopa.payhub.pdnd.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.payhub.pdnd.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.client.generated.PdndClientApi;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
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
class PdndClientClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private PdndClientApi pdndClientApiMock;

  private PdndClientClient pdndClientClient;

  @BeforeEach
  void setUp() {
    pdndClientClient = new PdndClientClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
            organizationApisHolderMock,
            pdndClientApiMock
    );
  }

  @Test
  void whenGetUsablePdndClientByOrganizationIdAndPdndServiceTypeThenOk() {
    // Given
    String accessToken = "ACCESS_TOKEN";
    Long organizationId = 1L;
    PdndServiceType pdndServiceType = PdndServiceType.SEND;
    String subUnitCode = "subUnitCode";
    PdndClientDTO expectedResult = new PdndClientDTO();

    when(organizationApisHolderMock.getPdndClientApi(accessToken))
            .thenReturn(pdndClientApiMock);
    when(pdndClientApiMock.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId, pdndServiceType, subUnitCode))
            .thenReturn(expectedResult);

    // When
    PdndClientDTO result = pdndClientClient.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId,pdndServiceType, subUnitCode, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNotFoundWhenGetUsablePdndClientByOrganizationIdAndPdndServiceTypeThenNull() {
    // Given
    String accessToken = "ACCESS_TOKEN";
    Long organizationId = 1L;
    PdndServiceType pdndServiceType = PdndServiceType.SEND;
    String subUnitCode = "subUnitCode";

    when(organizationApisHolderMock.getPdndClientApi(accessToken))
            .thenReturn(pdndClientApiMock);
    when(pdndClientApiMock.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId, pdndServiceType, subUnitCode))
            .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    // When
    PdndClientDTO result = pdndClientClient.getUsablePdndClientByOrganizationIdAndPdndServiceType(organizationId,pdndServiceType, subUnitCode, accessToken);

    // Then
    Assertions.assertNull(result);
  }
}