package it.gov.pagopa.payhub.pdnd.anpr.connector.c003.config;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.anpr.services.c003")
public class AnprC003ServiceConfig extends PdndServiceIntegratedConfig {

}
