package it.gov.pagopa.payhub.pdnd.config.pdnd.anpr;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegrationConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.pdnd.anpr.services.c030")
public class AnprC030ServiceConfig extends PdndServiceIntegrationConfig {

}
