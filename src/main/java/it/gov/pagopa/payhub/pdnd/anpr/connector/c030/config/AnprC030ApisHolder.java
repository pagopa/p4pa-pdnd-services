package it.gov.pagopa.payhub.pdnd.anpr.connector.c030.config;

import it.gov.pagopa.anpr.c030.client.generated.E002ServiceApi;
import it.gov.pagopa.anpr.c030.dto.generated.RispostaKO;
import it.gov.pagopa.anpr.c030.generated.ApiClient;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.anpr.connector.BaseAnprServiceApisHolder;
import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

import java.util.stream.Collectors;

@Slf4j
@Service
public class AnprC030ApisHolder extends BaseAnprServiceApisHolder<ApiClient> {

    private final E002ServiceApi e002ServiceApi;

    public AnprC030ApisHolder(
            PdndApiClientConfig pdndApiClientConfig,
            AnprApiClientConfig clientConfig,
            RestTemplateBuilder restTemplateBuilder,
            HttpClientConfig defaultHttpClientConfig,
            JsonMapper jsonMapper
    ) {
        super(
                pdndApiClientConfig.getConfig(), clientConfig,
                restTemplateBuilder,
                defaultHttpClientConfig, clientConfig.getServices().getC030(),
                new HttpClientErrorJsonBodyHandler<>(jsonMapper, "ANPR-C030", clientConfig.isPrintBodyWhenError(),
                        RispostaKO.class, null,
                        r -> CollectionUtils.isEmpty(r.getListaErrori())
                                ? "UNKNOWN ERROR"
                                : r.getListaErrori().stream()
                                .map(e -> e.getCodiceErroreAnomalia() + " - " + e.getTestoErroreAnomalia())
                                .collect(Collectors.joining(","))
                )
        );

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
