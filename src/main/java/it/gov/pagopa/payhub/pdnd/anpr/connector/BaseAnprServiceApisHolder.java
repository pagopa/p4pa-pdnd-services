package it.gov.pagopa.payhub.pdnd.anpr.connector;

import it.gov.pagopa.payhub.pdnd.config.RestTemplateConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Slf4j
@Service
public abstract class BaseAnprServiceApisHolder <T> {

    protected final T apiClient;

    protected final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

    protected BaseAnprServiceApisHolder(
            AnprApiClientConfig clientConfig,
            PdndServiceIntegratedConfig anprServiceConfig,
            RestTemplateBuilder restTemplateBuilder
    ) {
        RestTemplate restTemplate = restTemplateBuilder.build(); // TODO HTTPS?
        if (clientConfig.isPrintBodyWhenError()) {
            restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("ANPR"));
        }
        // TODO pdnd headers https://github.com/pagopa/idpay-admissibility-assessor/blob/main/src/main/java/it/gov/pagopa/common/reactive/pdnd/service/BaseRestPdndServiceClient.java#L73

        this.apiClient = buildApiClient(restTemplate);
        setApiClientBasePath(apiClient, clientConfig.getBaseUrl() + anprServiceConfig.getBasePath());
        setApiClientBearerToken(apiClient, bearerTokenHolder::get);
        setApiClientMaxAttemptsForRetry(apiClient, Math.max(1, clientConfig.getMaxAttempts()));
        setApiClientWaitTimeMillis(apiClient, clientConfig.getWaitTimeMillis());
    }

    // Configure openApiGenerator ApiClient
    protected abstract T buildApiClient(RestTemplate restTemplate);
    protected abstract void setApiClientBasePath(T apiClient, String basePath);
    protected abstract void setApiClientBearerToken(T apiClient, Supplier<String> accessTokenSupplier);
    protected abstract void setApiClientMaxAttemptsForRetry(T apiClient, int maxAttemptsForRetry);
    protected abstract void setApiClientWaitTimeMillis(T apiClient, long waitTimeMillis);

    @PreDestroy
    public void unload() {
        bearerTokenHolder.remove();
    }

}
