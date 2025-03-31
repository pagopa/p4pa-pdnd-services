package it.gov.pagopa.payhub.pdnd.connector.pdnd.config;

import it.gov.pagopa.payhub.pdnd.config.RestTemplateConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.ApiClient;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.generated.api.AuthApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class PdndApisHolder {

    private final AuthApi authApi;

    public PdndApisHolder(
            PdndApiClientConfig clientConfig,
            RestTemplateBuilder restTemplateBuilder
    ) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(clientConfig.getBaseUrl());
        apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
        apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
        if (clientConfig.isPrintBodyWhenError()) {
            restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("PDND"));
        }

        this.authApi = new AuthApi(apiClient);
    }

    public AuthApi getBrokerEntityControllerApi() {
        return authApi;
    }

}
