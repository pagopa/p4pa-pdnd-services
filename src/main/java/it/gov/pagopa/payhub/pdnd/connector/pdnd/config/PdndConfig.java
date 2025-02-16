package it.gov.pagopa.payhub.pdnd.connector.pdnd.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.pdnd.config")
@Data
public class PdndConfig {
    private String audience;
}
