package it.gov.pagopa.payhub.pdnd.connector.pdnd;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndAuthDataBuilderService;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndServiceIntegratedConfigResolverService;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PdndServiceImpl implements PdndService {

    private final PdndAuthDataBuilderService pdndAuthDataBuilderService;
    private final PdndServiceIntegratedConfigResolverService pdndServiceIntegratedConfigResolverService;
    protected final ConcurrentHashMap<Triple<PdndServiceType,Long,String>, PdndAuthData> jwtCache = new ConcurrentHashMap<>();

    public PdndServiceImpl(PdndAuthDataBuilderService pdndAuthDataBuilderService, PdndServiceIntegratedConfigResolverService pdndServiceIntegratedConfigResolverService) {
        this.pdndAuthDataBuilderService = pdndAuthDataBuilderService;
        this.pdndServiceIntegratedConfigResolverService = pdndServiceIntegratedConfigResolverService;
    }

    @Override
    public PdndAuthData generateToken(PdndServiceType pdndServicesEnum, Long organizationId, String subUnitCode, String accessToken) {
        return jwtCache.compute(Triple.of(pdndServicesEnum,organizationId,subUnitCode), (key, pdndAuthData) -> {
            log.debug("Check cache for token exists and not expired for triple PdndServiceEnum {} organizationId {} subUnitCode {}", pdndServicesEnum, organizationId, subUnitCode);
            if (pdndAuthData == null || pdndAuthData.getExpiration().isBefore(LocalDateTime.now())) {
                PdndServiceIntegratedConfig pdndServiceIntegratedConfig = pdndServiceIntegratedConfigResolverService.getPdndServiceIntegratedConfig(pdndServicesEnum,organizationId,subUnitCode,accessToken);
                log.debug("Token for {} not present or expired, generate new one", pdndServiceIntegratedConfig.getClass().getName());
                return pdndAuthDataBuilderService.build(pdndServiceIntegratedConfig);
            }
            log.debug("Token for triple PdndServiceEnum {} organizationId {} subUnitCode {} is present in cache", pdndServicesEnum, organizationId, subUnitCode);
            return pdndAuthData;
        });
    }
}
