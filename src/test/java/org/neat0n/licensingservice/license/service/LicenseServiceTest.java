package org.neat0n.licensingservice.license.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.neat0n.licensingservice.license.config.ServiceConfig;
import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.repo.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LicenseServiceTest {
    @Autowired
    MessageSource messages;
    @MockBean
    private LicenseRepository licenseRepository;
    @MockBean
    private License license;
    @MockBean
    ServiceConfig serviceConfig;
    
    @Autowired
    LicenseService licenseService;
    
    private String organizationId;
    private String licenseId;
    
    
    //messages
    @Value("${license.create.error.message}")
    private String createErrorMessage;
    
    @BeforeEach
    void setUp() {
        this.organizationId = "Meh";
        this.licenseId = UUID.randomUUID().toString();
        
        this.license.setLicenseId(licenseId);
        this.license.setOrganizationId(organizationId);
        
        Mockito.when(serviceConfig.getProperty()).thenReturn("someComment");
    }
    
    @Test
    void getLicense() {
        
        Mockito.when(licenseRepository.findByOrganizationIdAndLicenseId(
                        this.license.getOrganizationId(),
                        this.license.getLicenseId()))
                .thenReturn(this.license);
        Mockito.when(license.withComment(this.serviceConfig.getProperty())).thenReturn(license);
        
        //when
        License returnedLicense = licenseService.getLicense(
                this.license.getLicenseId(), this.license.getOrganizationId());
        //then
        assertEquals(license, returnedLicense);
    }
    
    @Test
    void getLicenseWillThrow() {
        Mockito.when(licenseRepository.findByOrganizationIdAndLicenseId(
                        this.license.getOrganizationId(),
                        this.license.getLicenseId()))
                .thenReturn(null);
        
        assertThrows(IllegalArgumentException.class,
                () -> licenseService.getLicense(
                        this.license.getLicenseId(),
                        this.license.getOrganizationId()));
    }
    
    @Test
    void createLicense() {
        //given
        Mockito.when(licenseRepository.save(this.license)).thenReturn(this.license);
        
        //when
        License returnedLicense = this.licenseService.createLicense(license, license.getOrganizationId());
        
        //then
        assertEquals(this.license, returnedLicense);
    }
    @Test
    void createLicenseWillThrowWithMessage(){
        Mockito.when(licenseRepository.save(license)).thenThrow(new Exception());
        assertThrows(IllegalArgumentException.class,
                () -> licenseService.createLicense(license, organizationId),
                String.format(messages.getMessage(this.createErrorMessage,null,null),
                        license, organizationId));
    }
    
    @Test
    void updateLicense() {
        License loadedLicense = licenseService.updateLicense(this.license, this.license.getOrganizationId());
        assertEquals(loadedLicense, license);
    }
    
    @Test
    void updateLicenseWithEmptyOrganizationId() {
        Mockito.when(license.getOrganizationId()).thenReturn(null);
        
        var organizationId = "cool!";
        
        License loadedLicense = licenseService.updateLicense(this.license, organizationId);
        assertEquals(loadedLicense, license);
        assertEquals(organizationId, loadedLicense.getOrganizationId());
    }
    
    @Test
    void deleteLicense() {
    
    }
}