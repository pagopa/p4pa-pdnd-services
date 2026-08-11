package it.gov.pagopa.payhub.pdnd.connector.pdnd.config;

import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.pdnd.generated.ApiClient;
import it.gov.pagopa.pdnd.client.generated.AuthApi;
import it.gov.pagopa.pdnd.dto.generated.ProblemDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

@Getter
@Slf4j
@Service
public class PdndApisHolder {

    private final AuthApi authApi;

    public PdndApisHolder(
            PdndApiClientConfig clientConfig,
            RestTemplateBuilder restTemplateBuilder,
            JsonMapper jsonMapper
    ) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(clientConfig.getBaseUrl());
        apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
        apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
        restTemplate.setErrorHandler(new HttpClientErrorJsonBodyHandler<>(jsonMapper, "PDND", clientConfig.isPrintBodyWhenError(),
                ProblemDTO.class, ProblemDTO::getTitle, ProblemDTO::getDetail)
        );

        this.authApi = new AuthApi(apiClient);
    }

}
