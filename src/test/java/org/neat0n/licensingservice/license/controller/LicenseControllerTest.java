package org.neat0n.licensingservice.license.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.repo.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
class LicenseControllerTest {
    private License license;
    @Autowired
    LicenseRepository repo;
    @Autowired
    MockMvc mvc;
    
    @BeforeAll
    void SetUp() {
        var license = new License();
        license.setOrganizationId(1);
        repo.save(license);
    }
    
    @Test
    void nothing() {
    
    }
}