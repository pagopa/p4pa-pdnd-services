package it.gov.pagopa.payhub.pdnd.config;

import java.time.Duration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {
    @Bean
    public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
      return configurer.configure(new RestTemplateBuilder())
          .setConnectTimeout(Duration.ofSeconds(120))
          .setReadTimeout(Duration.ofSeconds(120));
    }
}
