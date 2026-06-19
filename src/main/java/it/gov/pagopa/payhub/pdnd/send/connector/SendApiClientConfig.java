package it.gov.pagopa.payhub.pdnd.send.connector;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedStaticConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.ApiClientConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.send")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class SendApiClientConfig extends ApiClientConfig {
  @NestedConfigurationProperty
  private PdndServiceIntegratedStaticConfig service;
}
