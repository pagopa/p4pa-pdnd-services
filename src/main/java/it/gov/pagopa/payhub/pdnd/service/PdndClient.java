package it.gov.pagopa.payhub.pdnd.service;

import com.nimbusds.jose.JOSEException;
import it.gov.pagopa.payhub.pdnd.dto.AccessTokenDTO;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.springframework.web.client.HttpClientErrorException;

public interface PdndClient {
  AccessTokenDTO getAccessToken() throws HttpClientErrorException, InvalidKeySpecException, NoSuchAlgorithmException, IOException, JOSEException;
}
