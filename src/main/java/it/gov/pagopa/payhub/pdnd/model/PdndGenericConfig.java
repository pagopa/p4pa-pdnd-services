package it.gov.pagopa.payhub.pdnd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdndGenericConfig {
  private String clientId;
  private String kid;
  private String purposeId;
}
