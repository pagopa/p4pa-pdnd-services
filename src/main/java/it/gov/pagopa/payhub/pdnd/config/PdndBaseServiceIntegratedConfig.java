package it.gov.pagopa.payhub.pdnd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class PdndBaseServiceIntegratedConfig {
  private String clientId;
  private String kid;
  private String purposeId;
}
