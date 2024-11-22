package it.gov.pagopa.payhub.pdnd.config;

import it.gov.pagopa.payhub.pdnd.model.PdndGenericConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.pdnd.anpr.config")
public class AnprConfig extends PdndGenericConfig {
}
