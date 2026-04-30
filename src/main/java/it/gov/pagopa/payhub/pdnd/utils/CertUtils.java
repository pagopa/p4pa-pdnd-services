package it.gov.pagopa.payhub.pdnd.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CertUtils {
    private CertUtils(){}

    public static RSAPublicKey pemPub2PublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        String pubStringFormat = extractInlinePemBody(publicKey);
        try(
                InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(pubStringFormat))
        ) {
            X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(is.readAllBytes());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(encodedKeySpec);
        }
    }

    /**
     * To generate a pair of RSA key you could use the following instructions:
     * openssl genrsa -out client-test-keypair.rsa.pem 2048
     * openssl rsa -in client-test-keypair.rsa.pem -pubout -out client-test-keypair.rsa.pub
     * openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in client-test-keypair.rsa.pem -out client-test-keypair.rsa.priv
     */
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
