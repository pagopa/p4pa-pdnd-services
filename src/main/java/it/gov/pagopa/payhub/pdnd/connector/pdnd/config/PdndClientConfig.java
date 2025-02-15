package it.gov.pagopa.payhub.pdnd.connector.pdnd.config;

import it.gov.pagopa.payhub.pdnd.connector.config.ClientConfig;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.pdnd")
@SuperBuilder
@NoArgsConstructor
public class PdndClientConfig extends ClientConfig {
}
