package it.gov.pagopa.payhub.pdnd.anpr.c030.service;

import it.gov.pagopa.payhub.anpr.C030.model.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.model.generated.RispostaE002OK;
import it.gov.pagopa.payhub.anpr.C030.model.generated.TipoCriteriRicercaE002;
import it.gov.pagopa.payhub.anpr.C030.model.generated.TipoDatiRichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.c030.client.AnprC030Client;
import it.gov.pagopa.payhub.pdnd.anpr.c030.client.AnprC030ClientImpl;
import org.springframework.stereotype.Service;

@Service
public class AnprC030Service {

    private final AnprC030Client anprC030Client;

    public AnprC030Service(AnprC030ClientImpl anprC030Client) {
        this.anprC030Client = anprC030Client;
    }

    public RispostaE002OK getIdAnprFromFc(String fiscalCode) {
        TipoCriteriRicercaE002 searchTypes = TipoCriteriRicercaE002.builder()
                .codiceFiscale(fiscalCode)
                .build();

        TipoDatiRichiestaE002 reqDataTypes = TipoDatiRichiestaE002.builder()
                .casoUso("C030")
                .build();

        RichiestaE002 request = RichiestaE002.builder()
                .idOperazioneClient("ID-ENTE-myHost-1701102800550")
                .criteriRicerca(searchTypes)
                .datiRichiesta(reqDataTypes)
                .build();

        return anprC030Client.getIdAnprFromFc(request);
    }
}

