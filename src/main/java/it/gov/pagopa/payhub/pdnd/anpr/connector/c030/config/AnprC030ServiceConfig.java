package it.gov.pagopa.payhub.pdnd.anpr.connector.c030.config;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.anpr.services.c030")
public class AnprC030ServiceConfig extends PdndServiceIntegratedConfig {

}
