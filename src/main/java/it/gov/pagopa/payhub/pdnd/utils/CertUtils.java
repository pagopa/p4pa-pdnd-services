package it.gov.pagopa.payhub.pdnd.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class CertUtils {
    private CertUtils(){}

    public static RSAPrivateKey pemKey2PrivateKey(String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String keyStringFormat =  extractInlinePemBody(privateKey);
        try(
                InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(keyStringFormat))
        ) {
            PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(is.readAllBytes());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(encodedKeySpec);
        }
    }

    public static String extractInlinePemBody(String target) {
        return target
                .replaceAll("^-----BEGIN[A-Z|\\s]+-----", "")
                .replaceAll("\\s+", "")
                .replaceAll("-----END[A-Z|\\s]+-----$", "");
    }
}
