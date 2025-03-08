package it.gov.pagopa.payhub.pdnd.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CryptoUtilsTest {

    @Test
    void whenSha256Base64ThenOk(){
        Assertions.assertEquals(
                "f9SGUoWD/kZFYdz81VpXWA9SCqyEw0hZXvnSdwuRRG8=",
                CryptoUtils.sha256Base64("PROVA"));
    }
}
