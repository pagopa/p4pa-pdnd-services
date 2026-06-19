package it.gov.pagopa.payhub.pdnd.anpr.connector.c030;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoCriteriRicercaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoDatiRichiestaE002;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.client.AnprC030Client;
import it.gov.pagopa.payhub.pdnd.anpr.service.AnprIdOperationGeneratorService;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@CacheConfig(cacheNames = it.gov.pagopa.payhub.pdnd.config.CacheConfig.Fields.anprIds)
public class AnprC030ServiceImpl implements AnprC030Service {

    private final AnprC030Client anprC030Client;
    private final PdndService pdndService;
    private final AnprIdOperationGeneratorService anprIdOperationGeneratorService;

    public AnprC030ServiceImpl(AnprC030Client anprC030Client, PdndService pdndService, AnprIdOperationGeneratorService anprIdOperationGeneratorService) {
        this.anprC030Client = anprC030Client;
        this.pdndService = pdndService;
        this.anprIdOperationGeneratorService = anprIdOperationGeneratorService;
    }

    @Override
    @Cacheable(key = "#fiscalCode", unless="#result == null")
    public RispostaE002OK getIdAnprFromFc(String fiscalCode, Long organizationId, String subUnitCode, String accessToken) {
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

        return anprC030Client.getIdAnprFromFc(
                request,
                pdndService.generateToken(
                        PdndServiceType.C030, organizationId, subUnitCode, accessToken
                )
        );
    }

}

