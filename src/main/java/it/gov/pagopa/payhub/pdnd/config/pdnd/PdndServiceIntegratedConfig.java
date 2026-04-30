package it.gov.pagopa.payhub.pdnd.config.pdnd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PdndServiceIntegratedConfig {
  private String basePath;
  private String purposeId;
  private String audience;
  private String clientId;
  private String kid;
  private String privateKey;
  private String publicKey;
}
