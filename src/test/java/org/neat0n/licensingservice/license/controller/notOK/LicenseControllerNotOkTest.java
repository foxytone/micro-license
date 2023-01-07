package org.neat0n.licensingservice.license.controller.notOK;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neat0n.licensingservice.exceptions.Errors;
import org.neat0n.licensingservice.exceptions.LicenseServiceException;
import org.neat0n.licensingservice.exceptions.interfaces.ExceptionToCauseConstructor;
import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.repo.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LicenseControllerNotOkTest {
    private final static String getUri = "/api/v1/organization/1/license/";
    private final static String putUri = "/api/v1/organization/1/license/";
    @Autowired
    private ObjectMapper mapper;
    private License license1;
    private License license2;
    @Autowired
    LicenseRepository licenseRepository;
    @Autowired
    ExceptionToCauseConstructor causeConstructor;

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
    void nonExistingLicense(){
        //given
        var licenseUuid = "146";
        var predictedException = new LicenseServiceException(licenseUuid, 1L, Errors.NO_LICENSE_FOUND);
        var predictedCause = causeConstructor.construct(predictedException);

        //then
        mvc.perform(
                get(getUri + licenseUuid)
        )
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(predictedCause)));
    }

    @Test
    @SneakyThrows
    void tooMuchProductName(){
        license1.setProductName("*********************************************************************");
        mvc.perform(
                put(putUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(license1))
        )
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    @SneakyThrows
    void wrongOrgId(){
        mvc.perform(
                get("/api/v1/organization/4/license/")
        ).andExpect(status().is4xxClientError());
        
        mvc.perform(
                get("/api/v1/organization/meh/license/")
        ).andExpect(status().is4xxClientError());
    }
}
