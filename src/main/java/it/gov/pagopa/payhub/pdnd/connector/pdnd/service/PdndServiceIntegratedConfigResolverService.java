package it.gov.pagopa.payhub.pdnd.connector.pdnd.service;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedStaticConfig;
import it.gov.pagopa.payhub.pdnd.connector.organization.PdndClientService;
import it.gov.pagopa.payhub.pdnd.connector.organization.PdndServiceService;
import it.gov.pagopa.payhub.pdnd.exception.custom.NotFoundException;
import it.gov.pagopa.payhub.pdnd.service.ApiClientConfigResolverService;
import it.gov.pagopa.payhub.pdnd.utils.ErrorCodeConstants;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PdndServiceIntegratedConfigResolverService {

  private final PdndServiceService pdndServiceService;
  private final PdndClientService pdndClientService;
  private final ApiClientConfigResolverService apiClientConfigResolverService;

  public PdndServiceIntegratedConfigResolverService(PdndServiceService pdndServiceService, PdndClientService pdndClientService,
                                                    ApiClientConfigResolverService apiClientConfigResolverService) {
    this.pdndServiceService = pdndServiceService;
    this.pdndClientService = pdndClientService;
      this.apiClientConfigResolverService = apiClientConfigResolverService;
  }

  public PdndServiceIntegratedConfig getPdndServiceIntegratedConfig(PdndServiceType pdndServicesEnum, Long organizationId, String subUnitCode, String accessToken) {
    PdndServiceIntegratedStaticConfig pdndServiceIntegratedStaticConfig = resolveStaticConfig(pdndServicesEnum);
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
            .basePath(pdndServiceIntegratedStaticConfig.getBasePath())
            .audience(pdndServiceIntegratedStaticConfig.getAudience())
            .purposeId(pdndService.getPurposeId())
            .clientId(pdndClient.getClientId())
            .kid(pdndClient.getKid())
            .privateKey(pdndClient.getPrivateKey())
            .publicKey(pdndClient.getPublicKey())
            .build();
  }

    private PdndServiceIntegratedStaticConfig resolveStaticConfig(PdndServiceType pdndServicesEnum) {
      PdndServiceIntegratedStaticConfig pdndServiceIntegratedStaticConfig = apiClientConfigResolverService.getIntegratedConfig(pdndServicesEnum);
      if(pdndServiceIntegratedStaticConfig == null){
        throw new IllegalStateException("Missing static configuration for serviceType "+pdndServicesEnum);
      }
      return pdndServiceIntegratedStaticConfig;
    }
}
