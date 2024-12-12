package it.gov.pagopa.payhub.pdnd.anpr.controller;

import it.gov.pagopa.payhub.controller.generated.DefaultApi;
import it.gov.pagopa.payhub.model.generated.Citizen;
import it.gov.pagopa.payhub.pdnd.anpr.service.PdndService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PdndServiceController implements DefaultApi {

    private final PdndService pdndService;

    public PdndServiceController(PdndService pdndService) {
        this.pdndService = pdndService;
    }

    @Override
    public ResponseEntity<Citizen> anprServiceE002CitizenGet(String fiscalCode) {
        log.info("call anprServiceE002CitizenGet");
        Citizen response = pdndService.getCitizenData(fiscalCode);
        return ResponseEntity.ok(response);
    }
}