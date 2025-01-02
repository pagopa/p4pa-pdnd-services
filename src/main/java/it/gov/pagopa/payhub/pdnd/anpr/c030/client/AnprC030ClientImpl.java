package it.gov.pagopa.payhub.pdnd.anpr.c030.client;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.c030.service.AnprC030ServiceConfig;
import it.gov.pagopa.payhub.pdnd.anpr.client.AbstractAnprClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
public class AnprC030ClientImpl extends AbstractAnprClient<RichiestaE002, RispostaE002OK> implements AnprC030Client {

    private final AnprC030ServiceConfig anprC030ServiceConfig;

    public AnprC030ClientImpl(RestTemplateBuilder restTemplateBuilder, AnprC030ServiceConfig anprC030ServiceConfig) {
        super(restTemplateBuilder);
        this.anprC030ServiceConfig = anprC030ServiceConfig;
    }

    @Override
    protected String getEndpointPath() {
        return anprC030ServiceConfig.getUrl();
    }

    @Override
    public RispostaE002OK getIdAnprFromFc(RichiestaE002 request) {
        return sendRequest(request, RispostaE002OK.class);
    }
}
