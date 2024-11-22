package it.gov.pagopa.payhub.pdnd.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import it.gov.pagopa.common.pdnd.generated.dto.ClientCredentialsResponseDTO;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClientImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;

@SpringBootTest
@EnableWireMock({
    @ConfigureWireMock(name = "pdnd")
})
@EnableConfigurationProperties
class PdndClientImplTest {

  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @InjectWireMock(value = "pdnd")
  private WireMockServer wireMockServer;

  private PdndClientImpl pdndClient;

  @BeforeEach
  void setup() {
      pdndClient = new PdndClientImpl(restTemplateBuilder, wireMockServer.baseUrl());
  }

  @Test
  void givenValidInputsWhenGetAccessTokenThenReturnResponse() {
    // Given
    String clientId = "CLIENTID";
    String assertions = "ASSERTION";

    // When
    ClientCredentialsResponseDTO response = pdndClient.getAccessToken(clientId, assertions);

    // Then
    Assertions.assertEquals("PDND_ACCESS_TOKEN", response.getAccessToken());
  }

}