package org.neat0n.licensingservice.license.controller.ok;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.repo.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.assertj.core.condition.Not.not;


@SpringBootTest
@AutoConfigureMockMvc
class LicenseControllerOkTest {
    private String getUri = "/api/v1/organization/1/license/";
    @Autowired
    private ObjectMapper mapper;
    private License license1;
    private License license2;
    @Autowired
    LicenseRepository licenseRepository;

    @Autowired
    MockMvc mvc;
    
    @BeforeEach
    void SetUp() {
        license1 = new License();
        license1.setOrganizationId(1L);
        licenseRepository.save(license1);

        license2 = new License();
        license2.setOrganizationId(1L);
        licenseRepository.save(license2);
    }
    @AfterEach
    void tearDown(){
        try{
            licenseRepository.deleteAll();
        }
        catch (org.hibernate.AssertionFailure ignored){
            //ignored on purpose
        }
    }
    
    @Test
    @SneakyThrows
    void getExistingLicense() {
        mvc.perform(
                get(getUri + license1.getUuid())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(license1)));

        mvc.perform(
                get(getUri + license2.getUuid())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(license2)));
    }
    @Test
    @SneakyThrows
    void license1NotEqualsLicense2(){
        mvc.perform(
                        get(getUri + license1.getUuid())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.uuid", is(not(license2.getUuid().toString()))))
                .andExpect(jsonPath("$.uuid", is(license1.getUuid().toString())));
    }

}