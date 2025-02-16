package it.gov.pagopa.payhub.pdnd.anpr.controller;

import it.gov.pagopa.payhub.pdnd.anpr.service.AnprService;
import it.gov.pagopa.payhub.pdnd.dto.generated.Address;
import it.gov.pagopa.payhub.pdnd.dto.generated.Citizen;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AnprController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {AnprController.class})
class AnprControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnprService anprService;

    @Test
    void givenValidFiscalCodeWhenGetCitizenDataThenReturnCitizenDetails() throws Exception {
        String fiscalCode = "DNTCRL65S67M126K";
        Citizen citizen = Citizen.builder()
                .firstName("Julieta")
                .lastName("Lindgren")
                .dateOfBirth("2024-12-11")
                .address(Address.builder()
                        .street("106 Hansen Mountains")
                        .city("West Aurelio")
                        .postalCode("36495-0217")
                        .country("Heard Island and McDonald Islands")
                        .build())
                .build();

        Mockito.when(anprService.getCitizenData(fiscalCode)).thenReturn(citizen);

        mockMvc.perform(get("/anpr-service-e002/citizen")
                        .param("fiscalCode", fiscalCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Julieta"))
                .andExpect(jsonPath("$.lastName").value("Lindgren"));

        Mockito.verify(anprService, Mockito.times(1)).getCitizenData(Mockito.any());
    }
}