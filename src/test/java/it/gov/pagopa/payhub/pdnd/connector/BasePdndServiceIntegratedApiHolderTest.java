package it.gov.pagopa.payhub.pdnd.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.crypto.RSASSASigner;
import it.gov.pagopa.payhub.pdnd.config.json.JsonConfig;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedAuthConfigurer;
import it.gov.pagopa.payhub.pdnd.config.pdnd.PdndServiceIntegratedConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.ApiClientConfig;
import it.gov.pagopa.payhub.pdnd.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.payhub.pdnd.connector.pdnd.config.PdndApiClientConfig;
import it.gov.pagopa.payhub.pdnd.dto.PdndAuthData;
import it.gov.pagopa.payhub.pdnd.utils.AgidUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
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

import static org.mockito.Mockito.*;

@Slf4j
public abstract class BasePdndServiceIntegratedApiHolderTest {

    @Mock
    private ClientHttpRequestExecution interceptorRequestExecutionMock;
    @Mock
    protected RestTemplate restTemplateMock;
    @Mock
    protected RSASSASigner jwsSignerMock;
    @Mock
    protected Void voidMock;

    protected void verifyHttpClientErrorJsonBodyHandlerConfiguration(Object api) {
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<HttpClientErrorJsonBodyHandler> captor = ArgumentCaptor.forClass(HttpClientErrorJsonBodyHandler.class);
        verify(restTemplateMock)
                .setErrorHandler(captor.capture());

        HttpClientErrorJsonBodyHandler<?> errorHandler = captor.getValue();
        String apiPackage = api
                .getClass().getPackageName()
                .replace(".client", "")
                .replace(".generated", "");

        Assertions.assertEquals(
                apiPackage,
                errorHandler.getErrorDtoClass().getPackageName()
                        .replace(".dto", "")
                        .replace(".generated", "")
        );

        Assertions.assertEquals(
                apiPackage
                        .replaceFirst("it\\.gov\\.pagopa\\.(pu\\.)?", "")
                        .replace(".", ""),
                errorHandler.getApplicationName()
                        .toLowerCase()
                        .replace("_","")
                        .replace("-", "")
        );
    }

    protected List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

    private final ObjectMapper objectMapper = new JsonConfig().objectMapper();

    @SuppressWarnings("unchecked")
    protected <T> void assertAuthenticationShouldBeSetInThreadSafeMode(
            PdndApiClientConfig.PdndConfig expectedPdndConfig,
            Function<PdndAuthData, T> apiInvoke,
            ParameterizedTypeReference<T> apiReturnedType,
            Runnable apiUnloader) throws InterruptedException, IOException, IllegalAccessException {

        ClientHttpRequestInterceptor reqInterceptor = interceptors.getFirst();
        Field pdndAuthDataSupplierField = ReflectionUtils.findField(PdndServiceIntegratedAuthConfigurer.class, "pdndAuthDataSupplier");
        Objects.requireNonNull(pdndAuthDataSupplierField).setAccessible(true);
        Supplier<PdndAuthData> pdndAuthDataSupplier = (Supplier<PdndAuthData>)pdndAuthDataSupplierField.get(reqInterceptor);

        PdndServiceIntegratedConfig pdndServiceIntegratedConfig = new PdndServiceIntegratedConfig();
        pdndServiceIntegratedConfig.setClientId("clientId");
        pdndServiceIntegratedConfig.setAudience("audience");
        pdndServiceIntegratedConfig.setKid("kid");
        pdndServiceIntegratedConfig.setBasePath("basePath");
        // Configuring useCases in a single thread
        List<Pair<PdndAuthData, T>> useCases = IntStream.rangeClosed(0, 100)
                .mapToObj(i -> {
                    try {
                        PdndAuthData pdndAuthData = new PdndAuthData(
                                "AGID_JWT_TRACKING_EVIDENCE_" + i,
                                "CLIENT_ASSERTION_" + i,
                                "ACCESS_TOKEN_" + i,
                                null,
                                pdndServiceIntegratedConfig,
                                pdndServiceIntegratedConfig.getAudience(),
                                jwsSignerMock
                        );
                        T expectedResult =
                                String.class.equals(apiReturnedType.getType()) ? (T) "RESULT"
                                        : Integer.class.equals(apiReturnedType.getType()) ? (T) Integer.valueOf(0)
                                        : Long.class.equals(apiReturnedType.getType()) ? (T) Long.valueOf(0L)
                                        : apiReturnedType.getType().getTypeName().startsWith(List.class.getName()) ? (T) List.of()
                                        : Void.class.equals(apiReturnedType.getType()) ? (T) voidMock
                                        : (T) mock(Class.forName(apiReturnedType.getType().getTypeName()));

                        doReturn(ResponseEntity.ok(expectedResult))
                                .when(restTemplateMock)
                                .exchange(
                                        Mockito.argThat(req -> {
                                            if(pdndAuthDataSupplier.get() != pdndAuthData){
                                                return false; // it's not the expected thread invocation
                                            }

                                            HttpHeaders headers = new HttpHeaders();
                                            String body;
                                            try (MockedStatic<AgidUtils> mockedStaticAgidUtils = Mockito.mockStatic(AgidUtils.class)) {
                                                mockedStaticAgidUtils.when(() ->
                                                                AgidUtils.buildAgidJwtSignature(Mockito.anyString(), Mockito.same(expectedPdndConfig.getAuthExpirationMinutes()),
                                                                        Mockito.same(jwsSignerMock), Mockito.same(pdndServiceIntegratedConfig)))
                                                        .thenAnswer(a -> "AGID_SIGNATURE_OF_DIGEST:" + a.getArgument(0));
                                                mockedStaticAgidUtils.when(() -> AgidUtils.buildDigest(Mockito.anyString()))
                                                        .thenCallRealMethod();

                                                body = objectMapper.writeValueAsString(req.getBody());
                                                byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

                                                HttpRequest httpRequestMock = mock(HttpRequest.class);
                                                when(httpRequestMock.getHeaders()).thenReturn(headers);

                                                reqInterceptor.intercept(httpRequestMock, bodyBytes, interceptorRequestExecutionMock);
                                                verify(interceptorRequestExecutionMock)
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

        verify(restTemplateMock, times(useCases.size()))
                .exchange(Mockito.any(), Mockito.<ParameterizedTypeReference<?>>any());
        verify(interceptorRequestExecutionMock, times(useCases.size()))
                .execute(Mockito.any(), Mockito.any());
    }

    /**
     * To assert if the ApiClient is working as expected. Set a test just once per *ApiHolder class (not for each exposed API)
     */
    protected <T> void assertRetry(ApiClientConfig apiClientConfig, Function<PdndAuthData, T> apiInvoke, ParameterizedTypeReference<T> apiReturnedType) {
        Assertions.assertTrue(apiClientConfig.getMaxAttempts() > 1, "Please set at least 2 max attempt");

        ResponseErrorHandler errorHandler = Mockito.mockingDetails(restTemplateMock)
                .getInvocations()
                .stream()
                .filter(i -> i.getMethod().getName().equals("setErrorHandler"))
                .map(i -> (ResponseErrorHandler) i.getArgument(0))
                .findFirst()
                .orElse(null);

        PdndServiceIntegratedConfig pdndServiceIntegratedConfig = new PdndServiceIntegratedConfig();
        pdndServiceIntegratedConfig.setClientId("clientId");
        pdndServiceIntegratedConfig.setAudience("audience");
        pdndServiceIntegratedConfig.setKid("kid");
        pdndServiceIntegratedConfig.setBasePath("basePath");

        PdndAuthData pdndAuthData = new PdndAuthData(
                "AGID_JWT_TRACKING_EVIDENCE",
                "CLIENT_ASSERTION",
                "ACCESS_TOKEN",
                null,
                pdndServiceIntegratedConfig,
                pdndServiceIntegratedConfig.getAudience(),
                jwsSignerMock
        );

        for (HttpStatus httpStatus : HttpStatus.values()) {
            if (httpStatus.is5xxServerError() || httpStatus.isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) {
                HttpStatusCodeException exception = httpStatus.is5xxServerError()
                        ? new HttpServerErrorException(httpStatus)
                        : new HttpClientErrorException(httpStatus);

                Mockito.doAnswer(i -> {
                            if (errorHandler != null) {
                                errorHandler.handleError(URI.create("http://example.com"), HttpMethod.GET, new MockClientHttpResponse(new byte[0], httpStatus));
                                return null;
                            } else {
                                throw exception;
                            }
                        })
                        .when(restTemplateMock)
                        .exchange(
                                Mockito.any(),
                                Mockito.eq(apiReturnedType));

                Assertions.assertThrows(RuntimeException.class, () -> apiInvoke.apply(pdndAuthData));

                try {
                    verify(restTemplateMock, times(apiClientConfig.getMaxAttempts()))
                            .exchange(Mockito.any(), Mockito.eq(apiReturnedType));
                    Mockito.clearInvocations(restTemplateMock);
                } catch (Throwable e) {
                    log.error("Error occurred verifying retry for httpStatus {}: {}", httpStatus, e.getMessage());
                    throw e;
                }
            }
        }
    }
}
