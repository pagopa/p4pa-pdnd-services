package it.gov.pagopa.payhub.pdnd.anpr.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public abstract class AbstractAnprClient<R, S> {

    @Value("${app.pdnd.anpr.base-url}")
    private String anprBasePath;

    private final RestTemplate restTemplate;

    protected AbstractAnprClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    protected abstract String getEndpointPath();

    public S sendRequest(R request, Class<S> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<R> entity = new HttpEntity<>(request, headers);
        log.info("ANPR url {}", anprBasePath + getEndpointPath());
        ResponseEntity<S> response = restTemplate.exchange(
                anprBasePath + getEndpointPath(),
                HttpMethod.POST,
                entity,
                responseType
        );

        return response.getBody();
    }
}

