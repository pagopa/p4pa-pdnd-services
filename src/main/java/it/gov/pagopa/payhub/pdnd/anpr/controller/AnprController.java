package it.gov.pagopa.payhub.pdnd.anpr.controller;

import it.gov.pagopa.payhub.pdnd.controller.generated.DefaultApi;
import it.gov.pagopa.payhub.pdnd.dto.generated.Citizen;
import it.gov.pagopa.payhub.pdnd.anpr.service.AnprService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnprController implements DefaultApi {

    private final AnprService anprService;

    public AnprController(AnprService anprService) {
        this.anprService = anprService;
    }

    @Override
    public ResponseEntity<Citizen> getCitizenDataByFiscalCode(String fiscalCode) {
        Citizen response = anprService.getCitizenData(fiscalCode);
        return ResponseEntity.ok(response);
    }
}