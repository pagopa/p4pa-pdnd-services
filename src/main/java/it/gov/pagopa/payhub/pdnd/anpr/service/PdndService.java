package it.gov.pagopa.payhub.pdnd.anpr.service;

import it.gov.pagopa.payhub.model.generated.AnprServiceE002CitizenGet200Response;

public interface PdndService {

    AnprServiceE002CitizenGet200Response getCitizenData(String fiscalCode);
}