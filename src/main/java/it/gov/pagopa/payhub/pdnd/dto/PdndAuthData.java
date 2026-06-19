package it.gov.pagopa.payhub.pdnd.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimbusds.jose.crypto.RSASSASigner;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PdndAuthData {
    private String agidJwtTrackingEvidence;
    private String clientAssertion;
    private String accessToken;
    private LocalDateTime expiration;
    @JsonIgnore
    private PdndServiceIntegratedConfig pdndServiceIntegratedConfig;
    private String serviceAudience;

    @JsonIgnore
    private RSASSASigner rsaJwsSigner;
}
