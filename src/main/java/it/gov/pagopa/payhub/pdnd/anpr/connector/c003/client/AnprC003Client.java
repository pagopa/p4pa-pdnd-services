package it.gov.pagopa.payhub.pdnd.anpr.connector.c003.client;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AbstractAnprClient;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c003.config.AnprC003ServiceConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
public class AnprC003Client extends AbstractAnprClient<RichiestaE002, RispostaE002OK> {

    private final AnprC003ServiceConfig anprC003ServiceConfig;

    public AnprC003Client(RestTemplateBuilder restTemplateBuilder, AnprC003ServiceConfig anprC003ServiceConfig) {
        super(restTemplateBuilder);
        this.anprC003ServiceConfig = anprC003ServiceConfig;
    }

    @Override
    protected String getEndpointPath() {
        return anprC003ServiceConfig.getUrl();
    }

    public RispostaE002OK getUserData(RichiestaE002 request) {
        return sendRequest(request, RispostaE002OK.class);
    }
}
