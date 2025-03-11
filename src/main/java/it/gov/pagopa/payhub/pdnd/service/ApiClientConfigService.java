package it.gov.pagopa.payhub.pdnd.service;

import it.gov.pagopa.payhub.pdnd.anpr.connector.AnprApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.send.connector.SendApiClientConfig;
import org.springframework.stereotype.Service;

@Service
public class ApiClientConfigService {

  private final SendApiClientConfig sendApiClientConfig;
  private final AnprApiClientConfig anprApiClientConfig;

  public ApiClientConfigService(SendApiClientConfig sendApiClientConfig,
      AnprApiClientConfig anprApiClientConfig) {
    this.sendApiClientConfig = sendApiClientConfig;
    this.anprApiClientConfig = anprApiClientConfig;
  }

  public PdndServiceIntegratedConfig getIntegratedConfig(String service){
    return switch (service) {
      case "SEND" -> sendApiClientConfig.getService();
      case "C003" -> anprApiClientConfig.getServices().getC003();
      case "C030" -> anprApiClientConfig.getServices().getC030();
      default -> null;
    };
  }
}
