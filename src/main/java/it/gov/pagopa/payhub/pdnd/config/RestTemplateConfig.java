package it.gov.pagopa.payhub.pdnd.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {
  private final int connectTimeoutMillis;
  private final int readTimeoutHandlerMillis;

  public RestTemplateConfig(
      @Value("${app.web-client.connect.timeout.millis}") int connectTimeoutMillis,
      @Value("${app.web-client.read.timeout.millis}") int readTimeoutHandlerMillis) {
    this.connectTimeoutMillis = connectTimeoutMillis;
    this.readTimeoutHandlerMillis = readTimeoutHandlerMillis;
  }

  @Bean
  public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
      return configurer.configure(new RestTemplateBuilder())
          .setConnectTimeout(Duration.ofMillis(connectTimeoutMillis))
          .setReadTimeout(Duration.ofMillis(readTimeoutHandlerMillis));
  }
}
