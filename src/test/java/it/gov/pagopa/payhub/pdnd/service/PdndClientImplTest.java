package it.gov.pagopa.payhub.pdnd.service;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.payhub.pdnd.dto.AccessTokenDTO;
import it.gov.pagopa.payhub.pdnd.utils.PdndUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class PdndClientImplTest {

  @InjectMocks
  private PdndClientImpl pdndClient;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private RestTemplateBuilder restTemplateBuilder;

  @Mock
  private PdndUtils pdndUtils;

  @Value("${app.pdnd.base-url}")
  private String pdndBaseUrl = "https://pdnd.it";

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
    pdndClient = new PdndClientImpl(restTemplateBuilder, pdndUtils, pdndBaseUrl);
  }

  @Test
  void whenGetAccessTokenThenSuccess() throws Exception {
    // Given
    String mockAssertion = "ASSERTION";
    AccessTokenDTO mockAccessToken = new AccessTokenDTO();
    mockAccessToken.setAccessToken("TOKEN");

    // When
    Mockito.when(pdndUtils.buildPdndClientAssertion()).thenReturn(mockAssertion);
    Mockito.when(restTemplate.postForObject(
        Mockito.eq(pdndBaseUrl + "/token.oauth2"),
        Mockito.any(HttpEntity.class),
        Mockito.eq(AccessTokenDTO.class)
    )).thenReturn(mockAccessToken);

    // Then
    AccessTokenDTO result = pdndClient.getAccessToken();
    assertEquals("TOKEN", result.getAccessToken());
  }

  @Test
  void whenGetAccessTokenThenException() throws Exception {
    // Given
    String mockAssertion = "ASSERTION";

    // When
    Mockito.when(pdndUtils.buildPdndClientAssertion()).thenReturn(mockAssertion);
    Mockito.when(restTemplate.postForObject(
        Mockito.eq(pdndBaseUrl + "/token.oauth2"),
        Mockito.any(HttpEntity.class),
        Mockito.eq(AccessTokenDTO.class)
    )).thenThrow(new RestClientException("Error during HTTP request"));

    // Then
    Exception exception = assertThrows(RestClientException.class, () -> pdndClient.getAccessToken());
    assertEquals("Error during HTTP request", exception.getMessage());
  }
}