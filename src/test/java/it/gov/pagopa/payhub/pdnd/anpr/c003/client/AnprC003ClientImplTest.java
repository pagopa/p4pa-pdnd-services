package it.gov.pagopa.payhub.pdnd.anpr.c003.client;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.*;
import it.gov.pagopa.payhub.pdnd.anpr.c003.service.AnprC003ServiceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AnprC003ClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private AnprC003ServiceConfig anprC003ServiceConfig;

    private AnprC003ClientImpl anprC003Client;

    @BeforeEach
    void setUp() throws Exception {
        try(AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {

            when(restTemplateBuilder.build()).thenReturn(restTemplate);

            anprC003Client = new AnprC003ClientImpl(restTemplateBuilder, anprC003ServiceConfig);
            ReflectionTestUtils.setField(anprC003Client, "anprBasePath", "http://localhost:8080");
        }
    }

    @Test
    void givenValidRequestWhenGetUserDataThenReturnValidResponse() {
        RichiestaE002 request = RichiestaE002.builder()
                .idOperazioneClient("13f32508-7bcb-38d0-8510-d68bf240aa59-1733496758205")
                .build();

        TipoInfoSoggettoEnte subTypeInfo = TipoInfoSoggettoEnte.builder()
                .id("nome")
                .chiave("Jed")
                .valore(TipoInfoValore.S)
                .valoreTesto("Nome del soggetto")
                .valoreData("2024-11-02")
                .dettaglio("")
                .build();

        TipoDatiSoggettiEnte subDataTypes = TipoDatiSoggettiEnte.builder()
                .infoSoggettoEnte(List.of(subTypeInfo))
                .build();

        TipoListaSoggetti subTypeList = TipoListaSoggetti.builder()
                .datiSoggetto(List.of(subDataTypes))
                .build();

        RispostaE002OK mockResponse = RispostaE002OK.builder()
                .idOperazioneANPR("ANPR-12345")
                .listaSoggetti(subTypeList)
                .build();

        ResponseEntity<RispostaE002OK> responseEntity = ResponseEntity.ok(mockResponse);

        when(anprC003ServiceConfig.getUrl()).thenReturn("/test");
        when(restTemplate.exchange(
                eq("http://localhost:8080/test"),
                eq(HttpMethod.POST),
                any(),
                eq(RispostaE002OK.class)
        )).thenReturn(responseEntity);

        RispostaE002OK result = anprC003Client.getUserData(request);

        assertEquals("ANPR-12345", result.getIdOperazioneANPR());
        assertEquals(1, result.getListaSoggetti().getDatiSoggetto().size());
        verify(restTemplate, times(1)).exchange(
                eq("http://localhost:8080/test"),
                eq(HttpMethod.POST),
                any(),
                eq(RispostaE002OK.class)
        );
    }
}