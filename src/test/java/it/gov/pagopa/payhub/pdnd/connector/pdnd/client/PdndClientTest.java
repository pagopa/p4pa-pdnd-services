package it.gov.pagopa.payhub.pdnd.connector.pdnd.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApisHolder;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.dto.ClientCredentialsResponseDTO;
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
class PdndClientTest {

  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @InjectWireMock(value = "pdnd")
  private WireMockServer wireMockServer;

  private PdndClient pdndClient;

  @BeforeEach
  void setup() {
    PdndApiClientConfig clientConfig = PdndApiClientConfig.builder()
            .baseUrl(wireMockServer.baseUrl())
            .build();
    pdndClient = new PdndClient(new PdndApisHolder(clientConfig, restTemplateBuilder));
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