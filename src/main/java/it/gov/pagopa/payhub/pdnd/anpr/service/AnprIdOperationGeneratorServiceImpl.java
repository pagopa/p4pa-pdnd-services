package it.gov.pagopa.payhub.pdnd.anpr.service;

import org.springframework.stereotype.Service;

@Service
public class AnprIdOperationGeneratorServiceImpl implements AnprIdOperationGeneratorService {
    /**
     * Required to be returned an always crescent id, if provided an oldValue, it would return the response previously associated to it.
     * This is a first attempt to resolve it, if the concurrency usage could increase, it should be replaced by a sequence stored on DB in order to guarantee always a different value
     */
    @Override
    public String generateId() {
        return String.valueOf(System.currentTimeMillis());
    }
}
