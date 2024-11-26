package it.gov.pagopa.payhub.pdnd.config.pdnd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class PdndServiceIntegrationConfig {
  private String clientId;
  private String kid;
  private String purposeId;
  private String privateKey;
  private String publicKey;
}
