package it.gov.pagopa.payhub.pdnd.anpr.service;

import it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoInfoSoggettoEnte;
import it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoInfoValore;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoDatiSoggettiEnte;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoIdentificativi;
import it.gov.pagopa.payhub.anpr.C030.dto.generated.TipoListaSoggetti;
import it.gov.pagopa.payhub.pdnd.dto.generated.Citizen;
import it.gov.pagopa.payhub.pdnd.anpr.c003.service.AnprC003Service;
import it.gov.pagopa.payhub.pdnd.anpr.c030.service.AnprC030Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdndServiceImplTest {

    @Mock
    private AnprC003Service anprC003Service;

    @Mock
    private AnprC030Service anprC030Service;

    @InjectMocks
    private PdndServiceImpl pdndService;

    @Test
    void givenValidFiscalCodeAndIdAnprWhenGetCitizenDataThenReturnCitizenDetails() {
        String fiscalCode = "DNTCRL65S67M126K";
        String idAnpr = "d20fcd8e-f228-323c-8924-6405b44879bf";

        TipoIdentificativi idTypes = TipoIdentificativi.builder()
                .idANPR(idAnpr)
                .build();

        TipoDatiSoggettiEnte subDataTypes = TipoDatiSoggettiEnte.builder()
                .identificativi(idTypes)
                .build();

        RispostaE002OK anprC030Response = newC030RispostaE002Ok();
        anprC030Response.setListaSoggetti(new TipoListaSoggetti(List.of(subDataTypes)));

        it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK anprC003Response = newC003RispostaE002Ok();
        TipoInfoSoggettoEnte subTypeInfo = TipoInfoSoggettoEnte.builder()
                .id("firstName")
                .chiave("Julieta")
                .valore(TipoInfoValore.S)
                .valoreTesto("First name of the subject")
                .valoreData("2024-11-02")
                .dettaglio("")
                .build();

        anprC003Response.setListaSoggetti(new it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoListaSoggetti(List.of(
                new it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoDatiSoggettiEnte(List.of(subTypeInfo))
        )));

        Mockito.when(anprC030Service.getIdAnprFromFc(fiscalCode)).thenReturn(anprC030Response);
        Mockito.when(anprC003Service.getUserData(idAnpr, fiscalCode)).thenReturn(anprC003Response);

        Citizen result = pdndService.getCitizenData(fiscalCode);

        assertNotNull(result);
        assertEquals("Julieta", result.getFirstName());
    }

    private static it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK newC003RispostaE002Ok() {
        return new it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK("IDOP", new it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoListaSoggetti(List.of()), List.of());
    }

    @Test
    void givenValidFiscalCodeAndInvalidSubjectListWhenGetCitizenDataThenThrowIllegalArgumentException() {
        String fiscalCode = "DNTCRL65S67M126K";
        String idAnpr = "d20fcd8e-f228-323c-8924-6405b44879bf";

        TipoIdentificativi idTypes = TipoIdentificativi.builder()
                .idANPR(idAnpr)
                .build();

        TipoDatiSoggettiEnte subDataTypes = TipoDatiSoggettiEnte.builder()
                .identificativi(idTypes)
                .build();

        RispostaE002OK anprC030Response = newC030RispostaE002Ok();
        anprC030Response.setListaSoggetti(new TipoListaSoggetti(List.of(subDataTypes)));

        it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK anprC003Response = newC003RispostaE002Ok();
        anprC003Response.setListaSoggetti(null);

        Mockito.when(anprC030Service.getIdAnprFromFc(fiscalCode)).thenReturn(anprC030Response);
        Mockito.when(anprC003Service.getUserData(idAnpr, fiscalCode)).thenReturn(anprC003Response);

        assertThrows(IllegalArgumentException.class, () -> pdndService.getCitizenData(fiscalCode));
    }

    private static RispostaE002OK newC030RispostaE002Ok() {
        return new RispostaE002OK("IDOP", new TipoListaSoggetti(List.of()), List.of());
    }

    @Test
    void givenValidFiscalCodeAndInvalidC003ResponseWhenGetCitizenDataThenThrowIllegalArgumentException() {
        String fiscalCode = "DNTCRL65S67M126K";
        String idAnpr = "d20fcd8e-f228-323c-8924-6405b44879bf";

        TipoIdentificativi idTypes = TipoIdentificativi.builder()
                .idANPR(idAnpr)
                .build();

        TipoDatiSoggettiEnte subDataTypes = TipoDatiSoggettiEnte.builder()
                .identificativi(idTypes)
                .build();

        RispostaE002OK anprC030Response = newC030RispostaE002Ok();
        anprC030Response.setListaSoggetti(new TipoListaSoggetti(List.of(subDataTypes)));

        Mockito.when(anprC030Service.getIdAnprFromFc(fiscalCode)).thenReturn(anprC030Response);
        Mockito.when(anprC003Service.getUserData(idAnpr, fiscalCode)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> pdndService.getCitizenData(fiscalCode));
    }

    @Test
    void givenValidFiscalCodeAndInvalidSubDataTypeWhenGetCitizenDataThenThrowIllegalArgumentException() {
        String fiscalCode = "DNTCRL65S67M126K";
        String idAnpr = "d20fcd8e-f228-323c-8924-6405b44879bf";

        TipoIdentificativi idTypes = TipoIdentificativi.builder()
                .idANPR(idAnpr)
                .build();

        TipoDatiSoggettiEnte subDataTypes = TipoDatiSoggettiEnte.builder()
                .identificativi(idTypes)
                .build();

        RispostaE002OK anprC030Response = newC030RispostaE002Ok();
        anprC030Response.setListaSoggetti(new TipoListaSoggetti(List.of(subDataTypes)));

        it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK anprC003Response = newC003RispostaE002Ok();

        anprC003Response.setListaSoggetti(new it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoListaSoggetti(null));

        Mockito.when(anprC030Service.getIdAnprFromFc(fiscalCode)).thenReturn(anprC030Response);
        Mockito.when(anprC003Service.getUserData(idAnpr, fiscalCode)).thenReturn(anprC003Response);

        assertThrows(IllegalArgumentException.class, () -> pdndService.getCitizenData(fiscalCode));
    }

    @Test
    void givenValidFiscalCodeAndEmptySubDataTypeWhenGetCitizenDataThenThrowIllegalArgumentException() {
        String fiscalCode = "DNTCRL65S67M126K";
        String idAnpr = "d20fcd8e-f228-323c-8924-6405b44879bf";

        TipoIdentificativi idTypes = TipoIdentificativi.builder()
                .idANPR(idAnpr)
                .build();

        TipoDatiSoggettiEnte subDataTypes = TipoDatiSoggettiEnte.builder()
                .identificativi(idTypes)
                .build();

        RispostaE002OK anprC030Response = newC030RispostaE002Ok();
        anprC030Response.setListaSoggetti(new TipoListaSoggetti(List.of(subDataTypes)));

        it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK anprC003Response = newC003RispostaE002Ok();

        anprC003Response.setListaSoggetti(new it.gov.pagopa.payhub.anpr.C003.dto.generated.TipoListaSoggetti(new ArrayList<>()));

        Mockito.when(anprC030Service.getIdAnprFromFc(fiscalCode)).thenReturn(anprC030Response);
        Mockito.when(anprC003Service.getUserData(idAnpr, fiscalCode)).thenReturn(anprC003Response);

        assertThrows(IllegalArgumentException.class, () -> pdndService.getCitizenData(fiscalCode));
    }

    @Test
    void givenServiceUnavailableWhenGetCitizenDataThenThrowRuntimeException() {
        String fiscalCode = "DNTCRL65S67M126K";

        Mockito.when(anprC030Service.getIdAnprFromFc(fiscalCode)).thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(RuntimeException.class, () -> pdndService.getCitizenData(fiscalCode));
    }
}