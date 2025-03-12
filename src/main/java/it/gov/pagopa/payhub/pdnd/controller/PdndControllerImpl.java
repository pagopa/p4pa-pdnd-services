package it.gov.pagopa.payhub.pdnd.controller;

import it.gov.pagopa.payhub.pdnd.connector.pdnd.PdndService;
import it.gov.pagopa.payhub.pdnd.controller.generated.P4paPdndApi;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.payhub.pdnd.service.ApiClientConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PdndControllerImpl implements P4paPdndApi {

  private final PdndService pdndService;
  private final ApiClientConfigService apiClientConfigService;

  public PdndControllerImpl(PdndService pdndService, ApiClientConfigService apiClientConfigService) {
    this.pdndService = pdndService;
    this.apiClientConfigService = apiClientConfigService;
  }

  @Override
  public ResponseEntity<PdndAuthData> getVoucherToken(String service) {
    log.info("Voucher requested for {} service", service);
    return ResponseEntity.ok(pdndService.generateToken(apiClientConfigService.getIntegratedConfig(service)));
  }
}
