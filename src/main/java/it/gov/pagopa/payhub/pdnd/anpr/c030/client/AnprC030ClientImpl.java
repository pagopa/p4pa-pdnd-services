package it.gov.pagopa.payhub.pdnd.anpr.c030.client;

import it.gov.pagopa.payhub.pdnd.anpr.c030.dto.RichiestaDTO;
import it.gov.pagopa.payhub.pdnd.anpr.c030.dto.RispostaOKDTO;
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

    public RispostaOKDTO getIdAnprFromCf(RichiestaDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<RichiestaDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<RispostaOKDTO> response = restTemplate.exchange(
                anprBasePath + anprC030ServiceConfig.getUrl(),
                HttpMethod.POST,
                entity,
                RispostaOKDTO.class
        );

        return response.getBody();
    }
}

