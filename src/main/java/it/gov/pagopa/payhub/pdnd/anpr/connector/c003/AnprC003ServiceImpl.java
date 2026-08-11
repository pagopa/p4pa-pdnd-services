package it.gov.pagopa.payhub.pdnd.anpr.connector.c003;

import it.gov.pagopa.anpr.c003.dto.generated.RichiestaE002;
import it.gov.pagopa.anpr.c003.dto.generated.RispostaE002OK;
import it.gov.pagopa.anpr.c003.dto.generated.TipoCriteriRicercaE002;
import it.gov.pagopa.anpr.c003.dto.generated.TipoDatiRichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c003.client.AnprC003Client;
import it.gov.pagopa.payhub.pdnd.anpr.service.AnprIdOperationGeneratorService;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.PdndService;
import it.gov.pagopa.payhub.pdnd.utils.Constants;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class AnprC003ServiceImpl implements AnprC003Service {

    private final AnprC003Client anprC003Client;
    private final PdndService pdndService;
    private final AnprIdOperationGeneratorService anprIdOperationGeneratorService;

    public AnprC003ServiceImpl(AnprC003Client anprC003Client, PdndService pdndService, AnprIdOperationGeneratorService anprIdOperationGeneratorService) {
        this.anprC003Client = anprC003Client;
        this.pdndService = pdndService;
        this.anprIdOperationGeneratorService = anprIdOperationGeneratorService;
    }

    @Override
    public RispostaE002OK getUserData(String idAnpr, Long organizationId, String subUnitCode, String accessToken) {
        TipoCriteriRicercaE002 searchTypes = TipoCriteriRicercaE002.builder()
                .idANPR(idAnpr)
                .build();

        TipoDatiRichiestaE002 reqDataTypes = TipoDatiRichiestaE002.builder()
                .dataRiferimentoRichiesta(DateTimeFormatter.ISO_DATE.format(LocalDate.now(Constants.ZONEID)))
                .motivoRichiesta("1")
                .casoUso("C003")
                .build();

        RichiestaE002 request = RichiestaE002.builder()
                .idOperazioneClient(anprIdOperationGeneratorService.generateId())
                .criteriRicerca(searchTypes)
                .datiRichiesta(reqDataTypes)
                .build();

        return anprC003Client.getUserData(
                request,
                pdndService.generateToken(
                        PdndServiceType.ANPR_C003,organizationId,subUnitCode,accessToken
                )
        );
    }
}

