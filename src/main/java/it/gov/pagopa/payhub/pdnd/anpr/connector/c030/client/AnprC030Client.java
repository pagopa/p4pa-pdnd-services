package it.gov.pagopa.payhub.pdnd.anpr.connector.c030.client;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AbstractAnprClient;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.config.AnprC030ServiceConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
public class AnprC030Client extends AbstractAnprClient<RichiestaE002, RispostaE002OK> {

    private final AnprC030ServiceConfig anprC030ServiceConfig;

    public AnprC030Client(RestTemplateBuilder restTemplateBuilder, AnprC030ServiceConfig anprC030ServiceConfig) {
        super(restTemplateBuilder);
        this.anprC030ServiceConfig = anprC030ServiceConfig;
    }

    @Override
    protected String getEndpointPath() {
        return anprC030ServiceConfig.getUrl();
    }

    public RispostaE002OK getIdAnprFromFc(RichiestaE002 request) {
        return sendRequest(request, RispostaE002OK.class);
    }
}
