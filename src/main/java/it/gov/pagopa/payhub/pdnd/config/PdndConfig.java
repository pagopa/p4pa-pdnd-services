package it.gov.pagopa.payhub.pdnd.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.pdnd.config")
@Data
public class PdndConfig {
    private String audience;
    private String clientId;
    private String kid;
    private String purposeId;
    private String key;
    private String publicKey;
}
