package it.gov.pagopa.payhub.pdnd.anpr.mapper;

import it.gov.pagopa.payhub.anpr.C003.model.generated.RispostaE002OK;
import it.gov.pagopa.payhub.anpr.C003.model.generated.TipoDatiSoggettiEnte;
import it.gov.pagopa.payhub.anpr.C003.model.generated.TipoInfoSoggettoEnte;
import it.gov.pagopa.payhub.model.generated.Address;
import it.gov.pagopa.payhub.model.generated.AnprServiceE002CitizenGet200Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnprResponseMapper {

    private AnprResponseMapper() {
    }

    public static AnprServiceE002CitizenGet200Response mapToAnprResponse(RispostaE002OK responseC003) {
        List<TipoInfoSoggettoEnte> subTypeInfo = getTipoInfoSoggettoEnteList(responseC003);

        Map<String, String> valuesMap = subTypeInfo.stream()
                .collect(Collectors.toMap(
                        TipoInfoSoggettoEnte::getId,
                        TipoInfoSoggettoEnte::getChiave
                ));

        Address address = Address.builder()
                .street(valuesMap.get("street"))
                .city(valuesMap.get("city"))
                .postalCode(valuesMap.get("postalCode"))
                .country(valuesMap.get("country"))
                .build();

        return AnprServiceE002CitizenGet200Response.builder()
                .firstName(valuesMap.get("firstName"))
                .lastName(valuesMap.get("lastName"))
                .dateOfBirth(valuesMap.get("dateOfBirth"))
                .address(address)
                .build();
    }

    private static List<TipoInfoSoggettoEnte> getTipoInfoSoggettoEnteList(RispostaE002OK responseC003) {
        if (responseC003 == null || responseC003.getListaSoggetti() == null) {
            throw new IllegalArgumentException("No valid data in listaSoggetti found in response");
        }

        List<TipoDatiSoggettiEnte> subDataTypes = responseC003.getListaSoggetti().getDatiSoggetto();
        if (subDataTypes == null || subDataTypes.isEmpty()) {
            throw new IllegalArgumentException("No subject data in datiSoggetto found in response");
        }

        return subDataTypes.getFirst().getInfoSoggettoEnte();
    }
}

