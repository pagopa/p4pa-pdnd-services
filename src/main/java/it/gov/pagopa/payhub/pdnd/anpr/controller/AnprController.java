package it.gov.pagopa.payhub.pdnd.anpr.controller;

import it.gov.pagopa.payhub.pdnd.anpr.service.AnprService;
import it.gov.pagopa.payhub.pdnd.controller.generated.AnprApi;
import it.gov.pagopa.payhub.pdnd.dto.generated.Citizen;
import it.gov.pagopa.payhub.pdnd.utils.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnprController implements AnprApi {

    private final AnprService anprService;

    public AnprController(AnprService anprService) {
        this.anprService = anprService;
    }

    @Override
    public ResponseEntity<Citizen> getCitizenDataByFiscalCode(String fiscalCode, Long organizationId, String subUnitCode) {
        Citizen response = anprService.getCitizenData(fiscalCode, organizationId, subUnitCode, SecurityUtils.getAccessToken());
        return ResponseEntity.ok(response);
    }
}