package it.gov.pagopa.payhub.pdnd.anpr.c030.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TipoCriteriRicercaDTO {

    private String codiceFiscale;

    private String cognome;

    private String senzaCognome;

    private String nome;

    private String senzaNome;

    private String sesso;

    private TipoDatiNascitaDTO datiNascita;

}
