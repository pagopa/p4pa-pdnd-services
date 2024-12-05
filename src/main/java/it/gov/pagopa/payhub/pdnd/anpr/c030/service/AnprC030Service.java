package it.gov.pagopa.payhub.pdnd.anpr.c030.service;

import it.gov.pagopa.payhub.pdnd.anpr.c030.client.AnprC030Client;
import it.gov.pagopa.payhub.pdnd.anpr.c030.client.AnprC030ClientImpl;
import it.gov.pagopa.payhub.pdnd.anpr.c030.dto.RichiestaDTO;
import it.gov.pagopa.payhub.pdnd.anpr.c030.dto.RispostaOKDTO;
import it.gov.pagopa.payhub.pdnd.anpr.c030.dto.TipoCriteriRicercaDTO;
import it.gov.pagopa.payhub.pdnd.anpr.c030.dto.TipoDatiRichiestaDTO;
import org.springframework.stereotype.Service;

@Service
public class AnprC030Service {

    private final AnprC030Client anprC030Client;

    public AnprC030Service(AnprC030ClientImpl anprC030Client) {
        this.anprC030Client = anprC030Client;
    }

    public RispostaOKDTO getIdAnprFromCf(String fiscalCode) {
        TipoCriteriRicercaDTO tipoCriteriRicercaDTO = TipoCriteriRicercaDTO.builder()
                .codiceFiscale(fiscalCode)
                .build();

        TipoDatiRichiestaDTO tipoDatiRichiestaDTO = TipoDatiRichiestaDTO.builder()
                .casoUso("C030")
                .build();

        RichiestaDTO request = RichiestaDTO.builder()
                .idOperazioneClient("ID-ENTE-myHost-1701102800550")
                .criteriRicerca(tipoCriteriRicercaDTO)
                .datiRichiesta(tipoDatiRichiestaDTO)
                .build();

        return anprC030Client.getIdAnprFromCf(request);
    }
}

