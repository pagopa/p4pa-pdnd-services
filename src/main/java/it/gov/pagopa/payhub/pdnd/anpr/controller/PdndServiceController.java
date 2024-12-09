package it.gov.pagopa.payhub.pdnd.anpr.controller;

import it.gov.pagopa.payhub.controller.generated.DefaultApi;
import it.gov.pagopa.payhub.model.generated.AnprServiceE002CitizenGet200Response;
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
    public ResponseEntity<AnprServiceE002CitizenGet200Response> anprServiceE002CitizenGet(String fiscalCode) {
        AnprServiceE002CitizenGet200Response response = pdndService.getCitizenData(fiscalCode);
        return ResponseEntity.ok(response);
    }
}