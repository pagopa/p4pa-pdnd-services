package it.gov.pagopa.payhub.pdnd.connector.pdnd.config;

import it.gov.pagopa.payhub.pdnd.config.ApiClientConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.pdnd")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class PdndApiClientConfig extends ApiClientConfig {
    @NestedConfigurationProperty
    private PdndConfig config;

    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @Data
    public static class PdndConfig {
        private String env;
        private String userId;
        private String audience;
        private long authExpirationMinutes;
    }
}
