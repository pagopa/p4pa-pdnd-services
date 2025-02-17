package it.gov.pagopa.payhub.pdnd.connector.pdnd.config;

import it.gov.pagopa.payhub.pdnd.config.ApiClientConfig;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.pdnd")
@SuperBuilder
@NoArgsConstructor
public class PdndApiClientConfig extends ApiClientConfig {
}
