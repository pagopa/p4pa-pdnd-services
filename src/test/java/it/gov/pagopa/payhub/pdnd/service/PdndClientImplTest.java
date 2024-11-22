package it.gov.pagopa.payhub.pdnd.service;

import it.gov.pagopa.payhub.pdnd.config.PdndConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.client.PdndClientImpl;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndClientAssertionBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class PdndClientImplTest {

  @InjectMocks
  private PdndClientImpl pdndClient;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private PdndConfig pdndConfig;

  @Mock
  private RestTemplateBuilder restTemplateBuilder;

  @Mock
  private PdndClientAssertionBuilderService pdndClientAssertionBuilderService;

  @Value("${app.pdnd.base-url}")
  private String pdndBaseUrl = "https://pdnd.it";

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
    pdndClient = new PdndClientImpl(restTemplateBuilder, pdndBaseUrl);
  }

}