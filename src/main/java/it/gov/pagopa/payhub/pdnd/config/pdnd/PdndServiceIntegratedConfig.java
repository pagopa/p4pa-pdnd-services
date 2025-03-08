package it.gov.pagopa.payhub.pdnd.config.pdnd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdndServiceIntegratedConfig {
  private String basePath;
  private String purposeId;
  private String clientId;
  private String kid;
  private String privateKey;
  private String publicKey;
}
