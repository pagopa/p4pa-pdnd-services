package it.gov.pagopa.payhub.pdnd.anpr.service;

import it.gov.pagopa.payhub.model.generated.Citizen;

public interface PdndService {

    Citizen getCitizenData(String fiscalCode);
}