package it.gov.pagopa.payhub.pdnd.config.pdnd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class PdndServiceIntegrationConfig {
  private String purposeId;
}
