package it.gov.pagopa.payhub.pdnd.anpr.connector.c003;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoCriteriRicercaE002;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoDatiRichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c003.client.AnprC003Client;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AnprC003ServiceImpl implements AnprC003Service {

    private final AnprC003Client anprC003Client;

    public AnprC003ServiceImpl(AnprC003Client anprC003Client) {
        this.anprC003Client = anprC003Client;
    }

    @Override
    public RispostaE002OK getUserData(String idAnpr, String fiscalCode) {
        TipoCriteriRicercaE002 searchTypes = TipoCriteriRicercaE002.builder()
                .idANPR(idAnpr)
                .build();

        TipoDatiRichiestaE002 reqDataTypes = TipoDatiRichiestaE002.builder()
                .casoUso("C003")
                .build();

        RichiestaE002 request = RichiestaE002.builder()
                .idOperazioneClient(generateIdClientOperation(fiscalCode))
                .criteriRicerca(searchTypes)
                .datiRichiesta(reqDataTypes)
                .build();

        return anprC003Client.getUserData(request);
    }

    private String generateIdClientOperation(String fiscalCode) {
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String uuid = UUID.nameUUIDFromBytes(fiscalCode.getBytes()).toString();
        return uuid + "-" + timestamp;
    }
}

