package it.gov.pagopa.payhub.pdnd.service;

import com.nimbusds.jose.JOSEException;
import it.gov.pagopa.payhub.pdnd.dto.AccessTokenDTO;
import it.gov.pagopa.payhub.pdnd.utils.PdndUtils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PdndClientImpl implements PdndClient {

  private static final String CLIENT_ASSERTION_TYPE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
  private static final String GRANT_TYPE = "client_credentials";
  private final RestTemplate restTemplate;
  private final PdndUtils pdndUtils;
  private final String pdndBaseUrl;

  public PdndClientImpl(RestTemplateBuilder restTemplateBuilder, PdndUtils pdndUtils,
      @Value("${app.pdnd.base-url}") String pdndBaseUrl) {
    this.restTemplate = restTemplateBuilder.build();
    this.pdndUtils = pdndUtils;
    this.pdndBaseUrl = pdndBaseUrl;
  }

  @Override
  public AccessTokenDTO getAccessToken()
      throws HttpClientErrorException, InvalidKeySpecException, NoSuchAlgorithmException, IOException, JOSEException {
    String clientAssertion = pdndUtils.buildPdndClientAssertion();

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", GRANT_TYPE);
    formData.add("client_assertion_type", CLIENT_ASSERTION_TYPE);
    formData.add("client_assertion", clientAssertion);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

    return restTemplate.postForObject(pdndBaseUrl+"/token.oauth2", request, AccessTokenDTO.class);
  }
}
