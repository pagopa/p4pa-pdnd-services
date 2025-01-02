package it.gov.pagopa.payhub.pdnd.anpr.controller;

import it.gov.pagopa.payhub.pdnd.controller.generated.DefaultApi;
import it.gov.pagopa.payhub.pdnd.dto.generated.Citizen;
import it.gov.pagopa.payhub.pdnd.anpr.service.PdndService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdndServiceController implements DefaultApi {

    private final PdndService pdndService;

    public PdndServiceController(PdndService pdndService) {
        this.pdndService = pdndService;
    }

    @Override
    public ResponseEntity<Citizen> getCitizenDataByFiscalCode(String fiscalCode) {
        Citizen response = pdndService.getCitizenData(fiscalCode);
        return ResponseEntity.ok(response);
    }
}