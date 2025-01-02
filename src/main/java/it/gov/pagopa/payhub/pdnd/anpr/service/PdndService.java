package it.gov.pagopa.payhub.pdnd.anpr.service;

import it.gov.pagopa.payhub.pdnd.dto.generated.Citizen;

public interface PdndService {

    Citizen getCitizenData(String fiscalCode);
}