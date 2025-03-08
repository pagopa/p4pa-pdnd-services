package it.gov.pagopa.payhub.pdnd.connector.pdnd;

import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.service.PdndAuthDataBuilderService;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PdndServiceImpl implements PdndService {

    private final PdndAuthDataBuilderService pdndAuthDataBuilderService;
    protected final ConcurrentHashMap<PdndServiceIntegratedConfig, PdndAuthData> jwtCache = new ConcurrentHashMap<>();

    public PdndServiceImpl(PdndAuthDataBuilderService pdndAuthDataBuilderService) {
        this.pdndAuthDataBuilderService = pdndAuthDataBuilderService;
    }

    @Override
    public PdndAuthData generateToken(PdndServiceIntegratedConfig pdndServiceIntegratedConfig) {
        return jwtCache.compute(pdndServiceIntegratedConfig, (key, pdndAuthData) -> {
            log.debug("Check cache for token exists and not expired for {}", pdndServiceIntegratedConfig.getClass().getName());
            if (pdndAuthData == null || pdndAuthData.getExpiration().isBefore(LocalDateTime.now())) {
                log.debug("Token for {} not present or expired, generate new one", pdndServiceIntegratedConfig.getClass().getName());
                return pdndAuthDataBuilderService.build(pdndServiceIntegratedConfig);
            }
            log.debug("Token for {} is present in cache", pdndServiceIntegratedConfig.getClass().getName());
            return pdndAuthData;
        });
    }
}
