package it.gov.pagopa.payhub.pdnd.controller;

import it.gov.pagopa.payhub.pdnd.connector.pdnd.PdndService;
import it.gov.pagopa.payhub.pdnd.controller.generated.P4paPdndApi;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.payhub.pdnd.utils.SecurityUtils;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class PdndControllerImpl implements P4paPdndApi {

  private final PdndService pdndService;

  public PdndControllerImpl(PdndService pdndService) {
    this.pdndService = pdndService;
  }

  @Override
  public ResponseEntity<PdndAuthData> getVoucherToken(PdndServiceType service, Long organizationId, String subUnitCode) {
    log.info("Voucher requested for service {} organizationId {} and subUnitCode {}", service, organizationId, subUnitCode);
    return ResponseEntity.ok(pdndService.generateToken(
            service, organizationId, subUnitCode, SecurityUtils.getAccessToken())
    );
  }
}