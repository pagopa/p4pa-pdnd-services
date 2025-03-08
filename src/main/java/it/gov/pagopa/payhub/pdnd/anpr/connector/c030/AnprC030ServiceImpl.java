package it.gov.pagopa.payhub.pdnd.anpr.connector.c030;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoCriteriRicercaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoDatiRichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.client.AnprC030Client;
import it.gov.pagopa.payhub.pdnd.anpr.service.AnprIdOperationGeneratorService;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.PdndService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class AnprC030ServiceImpl implements AnprC030Service {

    private final AnprC030Client anprC030Client;
    private final PdndServiceIntegratedConfig anprC030ServiceConfig;
    private final PdndService pdndService;
    private final AnprIdOperationGeneratorService anprIdOperationGeneratorService;

    public AnprC030ServiceImpl(AnprC030Client anprC030Client, AnprApiClientConfig anprApiClientConfig, PdndService pdndService, AnprIdOperationGeneratorService anprIdOperationGeneratorService) {
        this.anprC030Client = anprC030Client;
        this.anprC030ServiceConfig = anprApiClientConfig.getServices().getC030();
        this.pdndService = pdndService;
        this.anprIdOperationGeneratorService = anprIdOperationGeneratorService;
    }

    public RispostaE002OK getIdAnprFromFc(String fiscalCode) {
        TipoCriteriRicercaE002 searchTypes = TipoCriteriRicercaE002.builder()
                .codiceFiscale(fiscalCode)
                .build();

        TipoDatiRichiestaE002 reqDataTypes = TipoDatiRichiestaE002.builder()
                .dataRiferimentoRichiesta(DateTimeFormatter.ISO_DATE.format(LocalDate.now()))
                .motivoRichiesta("1")
                .casoUso("C030")
                .build();

        RichiestaE002 request = RichiestaE002.builder()
                .idOperazioneClient(anprIdOperationGeneratorService.generateId())
                .criteriRicerca(searchTypes)
                .datiRichiesta(reqDataTypes)
                .build();

        return anprC030Client.getIdAnprFromFc(request, pdndService.generateToken(anprC030ServiceConfig));
    }

}

