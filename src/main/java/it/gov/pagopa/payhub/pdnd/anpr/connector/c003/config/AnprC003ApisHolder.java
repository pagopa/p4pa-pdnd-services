package it.gov.pagopa.payhub.pdnd.anpr.connector.c003.config;

import it.gov.pagopa.payhub.anpr.C003.controller.ApiClient;
import it.gov.pagopa.payhub.anpr.C003.controller.generated.E002ServiceApi;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.anpr.connector.BaseAnprServiceApisHolder;
import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class AnprC003ApisHolder extends BaseAnprServiceApisHolder<ApiClient> {

    private final E002ServiceApi e002ServiceApi;

    public AnprC003ApisHolder(
            PdndApiClientConfig pdndApiClientConfig,
            AnprApiClientConfig clientConfig,
            RestTemplateBuilder restTemplateBuilder,
            HttpClientConfig defaultHttpClientConfig
    ) {
        super(pdndApiClientConfig.getConfig(), clientConfig, restTemplateBuilder, defaultHttpClientConfig, clientConfig.getServices().getC003());

        this.e002ServiceApi = new E002ServiceApi(apiClient);
    }

//region Configure openApiGenerator ApiClient
    @Override
    protected ApiClient buildApiClient(RestTemplate restTemplate) {
        return new ApiClient(restTemplate);
    }

    @Override
    protected void setApiClientBasePath(ApiClient apiClient, String basePath) {
        apiClient.setBasePath(basePath);
    }

    @Override
    protected void setApiClientMaxAttemptsForRetry(ApiClient apiClient, int maxAttemptsForRetry) {
        apiClient.setMaxAttemptsForRetry(maxAttemptsForRetry);
    }

    @Override
    protected void setApiClientWaitTimeMillis(ApiClient apiClient, long waitTimeMillis) {
        apiClient.setWaitTimeMillis(waitTimeMillis);
    }
//endregion

    public E002ServiceApi getE002ServiceApi(PdndAuthData pdndAuthData) {
        return getApi(pdndAuthData, e002ServiceApi);
    }

}
