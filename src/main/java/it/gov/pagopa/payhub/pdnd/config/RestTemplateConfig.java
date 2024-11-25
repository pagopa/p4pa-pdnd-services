package it.gov.pagopa.payhub.pdnd.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RestTemplateConfig {
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
      log.debug("settings RestTemplate timeout to 120 sec");
      return builder
          .setConnectTimeout(Duration.ofMillis(120000))
          .setReadTimeout(Duration.ofMillis(120000))
          .build();
  }
}
