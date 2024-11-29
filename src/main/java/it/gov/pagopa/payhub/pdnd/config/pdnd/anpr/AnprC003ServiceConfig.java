package it.gov.pagopa.payhub.pdnd.config.pdnd.anpr;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.pdnd.anpr.services.c003")
public class AnprC003ServiceConfig extends PdndServiceIntegratedConfig {

}
