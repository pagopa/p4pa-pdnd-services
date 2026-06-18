package it.gov.pagopa.payhub.pdnd.anpr.connector;

import it.gov.pagopa.payhub.pdnd.config.rest.ApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.HttpsClientConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.anpr")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class AnprApiClientConfig extends ApiClientConfig {
    @NestedConfigurationProperty
    private HttpsClientConfig https;
}
