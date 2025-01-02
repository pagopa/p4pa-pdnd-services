package it.gov.pagopa.payhub.pdnd.anpr.c003.client;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.c003.service.AnprC003ServiceConfig;
import it.gov.pagopa.payhub.pdnd.anpr.client.AbstractAnprClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
public class AnprC003ClientImpl extends AbstractAnprClient<RichiestaE002, RispostaE002OK> implements AnprC003Client {

    private final AnprC003ServiceConfig anprC003ServiceConfig;

    public AnprC003ClientImpl(RestTemplateBuilder restTemplateBuilder, AnprC003ServiceConfig anprC003ServiceConfig) {
        super(restTemplateBuilder);
        this.anprC003ServiceConfig = anprC003ServiceConfig;
    }

    @Override
    protected String getEndpointPath() {
        return anprC003ServiceConfig.getUrl();
    }

    @Override
    public RispostaE002OK getUserData(RichiestaE002 request) {
        return sendRequest(request, RispostaE002OK.class);
    }
}
