package it.gov.pagopa.payhub.pdnd.utils;

import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientConfig;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.boot.http.client.HttpComponentsClientHttpRequestFactoryBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class SSLUtils {
    private SSLUtils() {
    }

    public static HttpComponentsClientHttpRequestFactoryBuilder buildTrustAllSSL(HttpClientConfig httpClientConfig) {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
            TlsSocketStrategy tlsSocketStrategy = (TlsSocketStrategy) ClientTlsStrategyBuilder.create()
                    .setSslContext(sslContext)
                    .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setTlsVersions(TLS.V_1_2, TLS.V_1_3)
                    .build();

            return HttpUtils.buildPooledConnection(httpClientConfig, tlsSocketStrategy);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new IllegalStateException("Cannot initialize SSL Context", e);
        }
    }
}
