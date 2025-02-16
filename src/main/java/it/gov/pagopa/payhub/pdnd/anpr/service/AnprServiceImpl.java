package it.gov.pagopa.payhub.pdnd.anpr.service;

import it.gov.pagopa.payhub.anpr.C030.dto.generated.RispostaE002OK;
import it.gov.pagopa.payhub.pdnd.dto.generated.Citizen;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c003.AnprC003Service;
import it.gov.pagopa.payhub.pdnd.anpr.connector.c030.AnprC030Service;
import it.gov.pagopa.payhub.pdnd.anpr.mapper.AnprResponseMapper;
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
    public Citizen getCitizenData(String fiscalCode) {
        RispostaE002OK anprC030Response = anprC030Service.getIdAnprFromFc(fiscalCode);
        String idAnpr = anprC030Response.getListaSoggetti()
                .getDatiSoggetto()
                .getFirst()
                .getIdentificativi()
                .getIdANPR();

        it.gov.pagopa.payhub.anpr.C003.dto.generated.RispostaE002OK anprC003Response = anprC003Service.getUserData(idAnpr, fiscalCode);

        return AnprResponseMapper.mapToAnprResponse(anprC003Response);
    }
}
