package it.gov.pagopa.payhub.pdnd.connector.organization.config;

import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.pu.organization.generated.ApiClient;
import it.gov.pagopa.pu.organization.generated.BaseApi;
import it.gov.pagopa.pu.organization.client.generated.PdndClientApi;
import it.gov.pagopa.pu.organization.client.generated.PdndServiceSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationErrorDTO;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

@Service
public class OrganizationApisHolder {

    private final PdndServiceSearchControllerApi pdndServiceSearchControllerApi;
    private final PdndClientApi pdndClientApi;

    private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

    public OrganizationApisHolder(
        OrganizationApiClientConfig clientConfig,
        RestTemplateBuilder restTemplateBuilder,
        JsonMapper jsonMapper
    ) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(clientConfig.getBaseUrl());
        apiClient.setBearerToken(bearerTokenHolder::get);
        apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
        apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
        restTemplate.setErrorHandler(new HttpClientErrorJsonBodyHandler<>(jsonMapper, "ORGANIZATION", clientConfig.isPrintBodyWhenError(),
                OrganizationErrorDTO.class, OrganizationErrorDTO::getCode, OrganizationErrorDTO::getMessage)
        );

        this.pdndClientApi = new PdndClientApi(apiClient);
        this.pdndServiceSearchControllerApi = new PdndServiceSearchControllerApi(apiClient);
    }

    @PreDestroy
    public void unload(){
        bearerTokenHolder.remove();
    }

    /** It will return a {@link PdndClientApi} instrumented with the provided accessToken. Use null if auth is not required */
    public PdndClientApi getPdndClientApi(String accessToken){
        return getApi(accessToken, pdndClientApi);
    }

    /** It will return a {@link PdndServiceSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required */
    public PdndServiceSearchControllerApi getPdndServiceSearchControllerApi(String accessToken){
        return getApi(accessToken, pdndServiceSearchControllerApi);
    }

    private <T extends BaseApi> T getApi(String accessToken, T api) {
        bearerTokenHolder.set(accessToken);
        return api;
    }
}
