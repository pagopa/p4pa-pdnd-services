package it.gov.pagopa.payhub.pdnd.anpr.service;

import org.springframework.stereotype.Service;

@Service
public class AnprIdOperationGeneratorServiceImpl implements AnprIdOperationGeneratorService {
    /** Required to be returned an always crescent id, if provided an oldValue, it would return the response previously associated to it */
    @Override
    public String generateId() {
        return String.valueOf(System.currentTimeMillis());
    }
}
