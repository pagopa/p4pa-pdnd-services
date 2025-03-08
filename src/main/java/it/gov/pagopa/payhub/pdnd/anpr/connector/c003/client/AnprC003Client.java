package it.gov.pagopa.payhub.pdnd.anpr.connector.c003.client;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c003.config.AnprC003ApisHolder;
import org.springframework.stereotype.Component;

@Component
public class AnprC003Client {

    private final AnprC003ApisHolder apisHolder;

    public AnprC003Client(AnprC003ApisHolder apisHolder) {
        this.apisHolder = apisHolder;
    }

    public RispostaE002OK getUserData(RichiestaE002 request, String accessToken) {
        return apisHolder.getE002ServiceApi(accessToken)
                .e002(request);
    }
}
