package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.organization.PdndClientService;
import it.gov.pagopa.payhub.pdnd.connector.organization.PdndServiceService;
import it.gov.pagopa.payhub.pdnd.exception.custom.NotFoundException;
import it.gov.pagopa.payhub.pdnd.utils.ErrorCodeConstants;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PdndIntegratedConfigResolverService {

  private final PdndServiceService pdndServiceService;
  private final PdndClientService pdndClientService;
  private final String anprC030BasePath;
  private final String anprC030Audience;
  private final String anprC003BasePath;
  private final String anprC003Audience;
  private final String sendBasePath;

  public PdndIntegratedConfigResolverService(PdndServiceService pdndServiceService, PdndClientService pdndClientService,
                                             @Value("${rest.anpr.services.c030.base-path}") String anprC030BasePath,
                                             @Value("${rest.anpr.services.c030.audience}") String anprC030Audience,
                                             @Value("${rest.anpr.services.c003.base-path}") String anprC003BasePath,
                                             @Value("${rest.anpr.services.c003.audience}") String anprC003Audience,
                                             @Value("${rest.send.service.base-path}") String sendBasePath) {
    this.pdndServiceService = pdndServiceService;
    this.pdndClientService = pdndClientService;
    this.anprC030BasePath = anprC030BasePath;
    this.anprC030Audience = anprC030Audience;
    this.anprC003BasePath = anprC003BasePath;
    this.anprC003Audience = anprC003Audience;
    this.sendBasePath = sendBasePath;
  }

  public PdndServiceIntegratedConfig getIntegratedConfig(PdndServiceType pdndServicesEnum, Long organizationId, String subUnitCode, String accessToken) {
    PdndClientDTO pdndClient = pdndClientService.getPdndClientByOrganizationIdAndPdndServiceType(
            organizationId,
            pdndServicesEnum,
            subUnitCode,
            accessToken
    );
    if (pdndClient == null) {
      throw new NotFoundException(ErrorCodeConstants.ERROR_CODE_PDND_CLIENT, "PdndClient having organizationId " + organizationId + " serviceType "+pdndServicesEnum+" and subUnitCode "+subUnitCode+" not found");
    }

    PdndService pdndService = pdndServiceService.findByClientIdAndServiceType(
            pdndClient.getClientId(),
            pdndServicesEnum,
            accessToken
    );
    if (pdndService == null) {
      throw new NotFoundException(ErrorCodeConstants.ERROR_CODE_PDND_SERVICE, "PdndService having clientId " + pdndClient.getClientId() + " not found");
    }

    return PdndServiceIntegratedConfig.builder()
            .basePath(resolveBasePath(pdndServicesEnum))
            .audience(resolveAudience(pdndServicesEnum))
            .purposeId(pdndService.getPurposeId())
            .clientId(pdndClient.getClientId())
            .kid(pdndClient.getKid())
            .privateKey(pdndClient.getPrivateKey())
            .publicKey(pdndClient.getPublicKey())
            .build();
  }

  private String resolveBasePath(PdndServiceType pdndServicesEnum) {
    return switch (pdndServicesEnum){
      case PdndServiceType.C030 -> anprC030BasePath;
      case PdndServiceType.C003 -> anprC003BasePath;
      case PdndServiceType.SEND -> sendBasePath;
    };
  }

  private String resolveAudience(PdndServiceType pdndServicesEnum) {
    return switch (pdndServicesEnum){
      case PdndServiceType.C030 -> anprC030Audience;
      case PdndServiceType.C003 -> anprC003Audience;
      case PdndServiceType.SEND -> null;
    };
  }
}
