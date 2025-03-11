package it.gov.pagopa.payhub.pdnd.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.crypto.RSASSASigner;
import it.gov.pagopa.payhub.pdnd.config.json.JsonConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedAuthConfigurer;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.utils.AgidUtils;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public abstract class BasePdndServiceIntegratedApiHolderTest {

    @Mock
    private ClientHttpRequestExecution interceptorRequestExecutionMock;
    @Mock
    protected RestTemplate restTemplateMock;
    @Mock
    protected RSASSASigner jwsSignerMock;
    @Mock
    protected Void voidMock;

    protected List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

    private final ObjectMapper objectMapper = new JsonConfig().objectMapper();

    @SuppressWarnings("unchecked")
    protected <T> void assertAuthenticationShouldBeSetInThreadSafeMode(
            PdndApiClientConfig.PdndConfig expectedPdndConfig,
            PdndServiceIntegratedConfig expectedPdndServiceIntegratedConfig,
            Function<PdndAuthData, T> apiInvoke,
            ParameterizedTypeReference<T> apiReturnedType,
            Runnable apiUnloader) throws InterruptedException, IOException, IllegalAccessException {

        ClientHttpRequestInterceptor reqInterceptor = interceptors.getFirst();
        Field pdndAuthDataSupplierField = ReflectionUtils.findField(PdndServiceIntegratedAuthConfigurer.class, "pdndAuthDataSupplier");
        Objects.requireNonNull(pdndAuthDataSupplierField).setAccessible(true);
        Supplier<PdndAuthData> pdndAuthDataSupplier = (Supplier<PdndAuthData>)pdndAuthDataSupplierField.get(reqInterceptor);

        // Configuring useCases in a single thread
        List<Pair<PdndAuthData, T>> useCases = IntStream.rangeClosed(0, 100)
                .mapToObj(i -> {
                    try {
                        PdndAuthData pdndAuthData = new PdndAuthData(
                                "AGID_JWT_TRACKING_EVIDENCE_" + i,
                                "CLIENT_ASSERTION_" + i,
                                "ACCESS_TOKEN_" + i,
                                null,
                                jwsSignerMock
                        );
                        T expectedResult =
                                String.class.equals(apiReturnedType.getType()) ? (T) "RESULT"
                                        : Integer.class.equals(apiReturnedType.getType()) ? (T) Integer.valueOf(0)
                                        : Long.class.equals(apiReturnedType.getType()) ? (T) Long.valueOf(0L)
                                        : apiReturnedType.getType().getTypeName().startsWith(List.class.getName()) ? (T) List.of()
                                        : Void.class.equals(apiReturnedType.getType()) ? (T) voidMock
                                        : (T) Mockito.mock(Class.forName(apiReturnedType.getType().getTypeName()));

                        Mockito.doReturn(ResponseEntity.ok(expectedResult))
                                .when(restTemplateMock)
                                .exchange(
                                        Mockito.argThat(req -> {
                                            if(pdndAuthDataSupplier.get() != pdndAuthData){
                                                return false; // it's not the expected thread invocation
                                            }

                                            HttpHeaders headers = new HttpHeaders();
                                            String body;
                                            try (MockedStatic<AgidUtils> mockedStaticAgidUtils = Mockito.mockStatic(AgidUtils.class)) {
                                                mockedStaticAgidUtils.when(() -> AgidUtils.buildAgidJwtSignature(Mockito.anyString(), Mockito.same(expectedPdndConfig.getAuthExpirationMinutes()), Mockito.same(expectedPdndServiceIntegratedConfig), Mockito.same(jwsSignerMock)))
                                                        .thenAnswer(a -> "AGID_SIGNATURE_OF_DIGEST:" + a.getArgument(0));
                                                mockedStaticAgidUtils.when(() -> AgidUtils.buildDigest(Mockito.anyString()))
                                                        .thenCallRealMethod();

                                                body = objectMapper.writeValueAsString(req.getBody());
                                                byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

                                                HttpRequest httpRequestMock = Mockito.mock(HttpRequest.class);
                                                Mockito.when(httpRequestMock.getHeaders()).thenReturn(headers);

                                                reqInterceptor.intercept(httpRequestMock, bodyBytes, interceptorRequestExecutionMock);
                                                Mockito.verify(interceptorRequestExecutionMock)
                                                        .execute(httpRequestMock, bodyBytes);
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }

                                            String accessToken = headers.getOrDefault(HttpHeaders.AUTHORIZATION, Collections.emptyList()).getFirst();
                                            String contentEncoding = headers.getOrDefault(HttpHeaders.CONTENT_ENCODING, Collections.emptyList()).getFirst();
                                            String agidTrackingEvidence = headers.getOrDefault("Agid-JWT-TrackingEvidence", Collections.emptyList()).getFirst();
                                            String digest = headers.getOrDefault("Digest", Collections.emptyList()).getFirst();
                                            String agidSignature = headers.getOrDefault("Agid-JWT-Signature", Collections.emptyList()).getFirst();

                                            String expectedDigest = AgidUtils.buildDigest(body);
                                            String expectedAgidSignature = "AGID_SIGNATURE_OF_DIGEST:" + expectedDigest;

                                            return accessToken.equals("Bearer " + pdndAuthData.getAccessToken()) &&
                                                    contentEncoding.equals("UTF-8") &&
                                                    agidTrackingEvidence.equals(pdndAuthData.getAgidJwtTrackingEvidence()) &&
                                                    digest.equals(expectedDigest) &&
                                                    agidSignature.equals(expectedAgidSignature);
                                        }),
                                        Mockito.eq(apiReturnedType));
                        return Pair.of(pdndAuthData, expectedResult);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .toList();

        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            executorService.invokeAll(useCases.stream()
                            .map(p -> (Callable<?>) () -> {
                                // Given
                                PdndAuthData pdndAuthData = p.getKey();
                                T expectedResult = p.getValue();

                                // When
                                T result = apiInvoke.apply(pdndAuthData);

                                // Then
                                Assertions.assertSame(expectedResult, result);
                                return true;
                            })
                            .toList())
                    .forEach(future -> {
                        try {
                            future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        apiUnloader.run();

        Mockito.verify(restTemplateMock, Mockito.times(useCases.size()))
                .exchange(Mockito.any(), Mockito.<ParameterizedTypeReference<?>>any());
        Mockito.verify(interceptorRequestExecutionMock, Mockito.times(useCases.size()))
                .execute(Mockito.any(), Mockito.any());
    }
}
