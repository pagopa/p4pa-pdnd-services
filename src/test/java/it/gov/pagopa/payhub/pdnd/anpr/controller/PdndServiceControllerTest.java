package it.gov.pagopa.payhub.pdnd.anpr.controller;

import it.gov.pagopa.payhub.model.generated.Address;
import it.gov.pagopa.payhub.model.generated.Citizen;
import it.gov.pagopa.payhub.pdnd.anpr.service.PdndService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PdndServiceController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {PdndServiceController.class})
class PdndServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PdndService pdndService;

    @Test
    void testAnprServiceE002CitizenGet_Success() throws Exception {
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

        Mockito.when(pdndService.getCitizenData(fiscalCode)).thenReturn(citizen);

        mockMvc.perform(get("/p4papdnd/anpr-service-e002/citizen")
                        .param("fiscalCode", fiscalCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Julieta"))
                .andExpect(jsonPath("$.lastName").value("Lindgren"));

        Mockito.verify(pdndService, Mockito.times(1)).getCitizenData(Mockito.any());
    }
}