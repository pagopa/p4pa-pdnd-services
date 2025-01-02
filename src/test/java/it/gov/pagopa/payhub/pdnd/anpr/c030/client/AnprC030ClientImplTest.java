package it.gov.pagopa.payhub.pdnd.anpr.c030.client;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.c030.service.AnprC030ServiceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AnprC030ClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private AnprC030ServiceConfig anprC030ServiceConfig;

    private AnprC030ClientImpl anprC030Client;

    @BeforeEach
    void setUp() throws Exception {
        try(AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {

            when(restTemplateBuilder.build()).thenReturn(restTemplate);

            anprC030Client = new AnprC030ClientImpl(restTemplateBuilder, anprC030ServiceConfig);
            ReflectionTestUtils.setField(anprC030Client, "anprBasePath", "http://localhost:8080");
        }
    }

    @Test
    void givenValidRequestWhenGetIdAnprFromFcThenReturnValidResponse() {
        RichiestaE002 request = RichiestaE002.builder()
                .idOperazioneClient("13f32508-7bcb-38d0-8510-d68bf240aa59-1733496758205")
                .build();

        RispostaE002OK mockResponse = RispostaE002OK.builder()
                .idOperazioneANPR("ANPR-12345")
                .build();

        ResponseEntity<RispostaE002OK> responseEntity = ResponseEntity.ok(mockResponse);

        when(anprC030ServiceConfig.getUrl()).thenReturn("/test");
        when(restTemplate.exchange(
                eq("http://localhost:8080/test"),
                eq(HttpMethod.POST),
                any(),
                eq(RispostaE002OK.class)
        )).thenReturn(responseEntity);

        RispostaE002OK result = anprC030Client.getIdAnprFromFc(request);

        assertEquals("ANPR-12345", result.getIdOperazioneANPR());
        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8080/test"),
                eq(HttpMethod.POST),
                any(),
                eq(RispostaE002OK.class)
        );
    }
}
