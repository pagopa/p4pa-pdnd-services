package it.gov.pagopa.payhub.pdnd.anpr.connector;

import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.RestTemplateConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedAuthConfigurer;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.payhub.pdnd.utils.SSLUtils;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public abstract class BaseAnprServiceApisHolder <T> {

    protected final T apiClient;

    protected final ThreadLocal<PdndAuthData> pdndAuthDataHolder = new ThreadLocal<>();

    protected BaseAnprServiceApisHolder(
            PdndApiClientConfig.PdndConfig pdndConfig,
            AnprApiClientConfig clientConfig,
            PdndServiceIntegratedConfig anprServiceConfig,
            RestTemplateBuilder restTemplateBuilder,
            HttpClientConfig httpClientConfig
    ) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.getInterceptors().add(new PdndServiceIntegratedAuthConfigurer(pdndConfig, anprServiceConfig, pdndAuthDataHolder::get));

        if(clientConfig.getHttps().isTrustAll()){
            restTemplate.setRequestFactory(SSLUtils.buildTrustAllSSL(httpClientConfig).build());
        }
        if (clientConfig.isPrintBodyWhenError()) {
            restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("ANPR"));
        }

        this.apiClient = buildApiClient(restTemplate);
        setApiClientBasePath(apiClient, clientConfig.getBaseUrl() + anprServiceConfig.getBasePath());
        setApiClientMaxAttemptsForRetry(apiClient, Math.max(1, clientConfig.getMaxAttempts()));
        setApiClientWaitTimeMillis(apiClient, clientConfig.getWaitTimeMillis());
    }

    // Configure openApiGenerator ApiClient
    protected abstract T buildApiClient(RestTemplate restTemplate);
    protected abstract void setApiClientBasePath(T apiClient, String basePath);
    protected abstract void setApiClientMaxAttemptsForRetry(T apiClient, int maxAttemptsForRetry);
    protected abstract void setApiClientWaitTimeMillis(T apiClient, long waitTimeMillis);

    @PreDestroy
    public void unload() {
        pdndAuthDataHolder.remove();
    }

    protected <A> A getApi(PdndAuthData pdndAuthData, A api) {
        pdndAuthDataHolder.set(pdndAuthData);
        return api;
    }
}
