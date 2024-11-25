package it.gov.pagopa.payhub.pdnd.config;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RestTemplateConfig {
  @Bean
  public RestTemplate restTemplate() {
    log.debug("settings RestTemplate timeout to 120 sec");
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(Duration.ofMillis(120000));
    factory.setReadTimeout(Duration.ofMillis(120000));

    return new RestTemplate(factory);
  }
}
