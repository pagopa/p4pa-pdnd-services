package it.gov.pagopa.payhub.pdnd.config.pdnd;

import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.utils.AgidUtils;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

/** It will intercept and configure PDND auth metadata */
public class PdndServiceIntegratedAuthConfigurer implements ClientHttpRequestInterceptor {

    private final PdndApiClientConfig.PdndConfig pdndConfig;
    private final PdndServiceIntegratedConfig pdndServiceIntegratedConfig;
    private final Supplier<PdndAuthData> pdndAuthDataSupplier;

    public PdndServiceIntegratedAuthConfigurer(PdndApiClientConfig.PdndConfig pdndConfig, PdndServiceIntegratedConfig pdndServiceIntegratedConfig, Supplier<PdndAuthData> pdndAuthDataSupplier) {
        this.pdndConfig = pdndConfig;
        this.pdndServiceIntegratedConfig = pdndServiceIntegratedConfig;
        this.pdndAuthDataSupplier = pdndAuthDataSupplier;
    }

    @Override
    @Nonnull
    public ClientHttpResponse intercept(@Nonnull HttpRequest request, @Nonnull byte[] body, @Nonnull ClientHttpRequestExecution execution) throws IOException {
        PdndAuthData pdndAuthData = pdndAuthDataSupplier.get();
        String digest = AgidUtils.buildDigest(new String(body, StandardCharsets.UTF_8));

        HttpHeaders headers = request.getHeaders();
        headers.add(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name());
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + pdndAuthData.getAccessToken());
        headers.add("Agid-JWT-TrackingEvidence", pdndAuthData.getAgidJwtTrackingEvidence());
        headers.add("Agid-JWT-Signature", AgidUtils.buildAgidJwtSignature(digest, pdndConfig, pdndServiceIntegratedConfig, pdndAuthData.getRsaJwsSigner()));
        headers.add("Digest", digest);

        return execution.execute(request, body);
    }
}
