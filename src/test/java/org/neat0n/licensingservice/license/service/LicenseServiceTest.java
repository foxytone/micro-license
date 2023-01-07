package org.neat0n.licensingservice.license.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.neat0n.licensingservice.config.ServiceConfig;
import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.repo.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
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
    
    private UUID licenseId;
    
    
    //messages
    @Value("${license.create.error.message}")
    private String createErrorMessage;
    
    @BeforeEach
    void setUp() {
        this.license.setOrganizationId(1L);
        Mockito.when(serviceConfig.getProperty()).thenReturn("someComment");
    }
    
    @Test
    @SneakyThrows
    void getLicense() {
        
        Mockito.when(licenseRepository.findByOrganizationIdAndUuid(
                        this.license.getOrganizationId(),
                        this.license.getUuid()))
                .thenReturn(Optional.of(this.license));
        Mockito.when(license.withComment(this.serviceConfig.getProperty())).thenReturn(license);
        
        //when
        License returnedLicense = licenseService.getLicense(
                this.license.getUuid(), this.license.getOrganizationId());
        //then
        assertEquals(license, returnedLicense);
    }
    
    @Test
    void getLicenseWillThrow() {
        Mockito.when(licenseRepository.findByOrganizationIdAndUuid(
                        this.license.getOrganizationId(),
                        this.license.getUuid()))
                .thenReturn(null);
        
        assertThrows(IllegalArgumentException.class,
                () -> licenseService.getLicense(
                        this.license.getUuid(),
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
    void createLicenseWillThrowWithMessage() {
        Mockito.when(licenseRepository.save(license)).thenThrow(new Exception());
        assertThrows(IllegalArgumentException.class,
                () -> licenseService.createLicense(license, license.getOrganizationId()),
                String.format(messages.getMessage(this.createErrorMessage, null, null),
                        license, license.getOrganizationId()));
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
        
        License loadedLicense = licenseService.updateLicense(this.license, license.getOrganizationId());
        assertEquals(loadedLicense, license);
        assertEquals(organizationId, loadedLicense.getOrganizationId());
    }
    
    @Test
    void deleteLicense() {
    
    }
}