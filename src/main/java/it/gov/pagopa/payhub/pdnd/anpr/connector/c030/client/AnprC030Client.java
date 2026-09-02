package it.gov.pagopa.payhub.pdnd.anpr.connector.c030.client;

import it.gov.pagopa.anpr.c030.dto.generated.RichiestaE002;
import it.gov.pagopa.anpr.c030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.config.AnprC030ApisHolder;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import org.springframework.stereotype.Component;

@Component
public class AnprC030Client {

    private final AnprC030ApisHolder apisHolder;

    public AnprC030Client(AnprC030ApisHolder apisHolder) {
        this.apisHolder = apisHolder;
    }

    public RispostaE002OK getIdAnprFromFc(RichiestaE002 request, PdndAuthData pdndAuthData) {
        return apisHolder.getE002ServiceApi(pdndAuthData)
                .e002(request);
    }
}
