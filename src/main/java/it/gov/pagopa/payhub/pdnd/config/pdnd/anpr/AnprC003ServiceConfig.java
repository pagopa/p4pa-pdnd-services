package it.gov.pagopa.payhub.pdnd.config.pdnd.anpr;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegrationConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.pdnd.anpr.service-c003.config")
public class AnprC003ServiceConfig extends PdndServiceIntegrationConfig {

}
