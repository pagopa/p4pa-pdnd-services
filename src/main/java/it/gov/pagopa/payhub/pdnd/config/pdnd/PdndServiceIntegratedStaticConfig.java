package it.gov.pagopa.payhub.pdnd.config.pdnd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PdndServiceIntegratedStaticConfig {
  private String basePath;
  private String audience;
}
