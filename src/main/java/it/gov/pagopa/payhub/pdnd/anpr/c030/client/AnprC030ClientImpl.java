package it.gov.pagopa.payhub.pdnd.anpr.c030.client;

import it.gov.pagopa.payhub.anpr.C030.model.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.model.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.c030.service.AnprC030ServiceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AnprC030ClientImpl implements AnprC030Client {

    @Value("${app.pdnd.anpr.base-url}")
    private String anprBasePath;
    private final RestTemplate restTemplate;
    private final AnprC030ServiceConfig anprC030ServiceConfig;

    public AnprC030ClientImpl(RestTemplateBuilder restTemplateBuilder, AnprC030ServiceConfig anprC030ServiceConfig) {
        this.restTemplate = restTemplateBuilder.build();
        this.anprC030ServiceConfig = anprC030ServiceConfig;
    }

    public RispostaE002OK getIdAnprFromFc(RichiestaE002 request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<RichiestaE002> entity = new HttpEntity<>(request, headers);

        ResponseEntity<RispostaE002OK> response = restTemplate.exchange(
                anprBasePath + anprC030ServiceConfig.getUrl(),
                HttpMethod.POST,
                entity,
                RispostaE002OK.class
        );

        return response.getBody();
    }
}

