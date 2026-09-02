package it.gov.pagopa.payhub.pdnd.anpr.service;

import it.gov.pagopa.anpr.c030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c003.AnprC003Service;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.AnprC030Service;
import it.gov.pagopa.payhub.pdnd.anpr.mapper.AnprResponseMapper;
import it.gov.pagopa.payhub.pdnd.dto.generated.Citizen;
import org.springframework.stereotype.Service;

@Service
public class AnprServiceImpl implements AnprService {

    private final AnprC003Service anprC003Service;
    private final AnprC030Service anprC030Service;

    public AnprServiceImpl(AnprC003Service anprC003Service, AnprC030Service anprC030Service) {
        this.anprC003Service = anprC003Service;
        this.anprC030Service = anprC030Service;
    }

    @Override
    public Citizen getCitizenData(String fiscalCode, Long organizationId, String subUnitCode, String accessToken) {
        RispostaE002OK anprC030Response = anprC030Service.getIdAnprFromFc(fiscalCode, organizationId, subUnitCode, accessToken);
        String idAnpr = anprC030Response.getListaSoggetti()
                .getDatiSoggetto()
                .getFirst()
                .getIdentificativi()
                .getIdANPR();

        it.gov.pagopa.anpr.c003.dto.generated.RispostaE002OK anprC003Response = anprC003Service.getUserData(idAnpr, organizationId, subUnitCode, accessToken);

        return AnprResponseMapper.mapToAnprResponse(anprC003Response);
    }
}
