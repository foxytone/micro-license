package org.neat0n.licensingservice.license.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.neat0n.licensingservice.config.ServiceConfig;
import org.neat0n.licensingservice.exceptions.Errors;
import org.neat0n.licensingservice.exceptions.LicenseServiceException;
import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.repo.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

@Service
@Log4j2
@PropertySource("classpath:messages_en.properties")
public class LicenseService {
    private final String serviceName = "licenseService";
    
    @Autowired
    MessageSource messages;
    @Autowired
    LicenseRepository licenseRepository;
    @Autowired
    ServiceConfig serviceConfig;
    
    //error messages block
    @Value("${license.search.error.message}")
    private String searchErrorMessage;
    @Value("${license.create.error.message}")
    private String createErrorMessage;
    @Value("${license.update.error.message}")
    private String updateErrorMessage;
    @Value("${license.delete.error.message}")
    private String deleteErrorMessage;
    //messages block
    @Value("${license.delete.licenseNotFound.message}")
    private String deleteLicenseNotFoundMessage;
    
    @Value("${license.delete.message}")
    private String deleteMessage;
    
    
    @CircuitBreaker(name = "licenseService")
    @SneakyThrows
    public License getLicense(UUID licenseId, long organizationId) {
        var license = licenseRepository
                .findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new LicenseServiceException(licenseId, organizationId, Errors.NO_LICENSE_FOUND));
        return license.withComment(serviceConfig.getProperty());
    }
    
    @CircuitBreaker(name = serviceName)
    public License createLicense(@NonNull License license, long organizationId) {
        license.setLicenseId(UUID.randomUUID());
        license.setOrganizationId(organizationId);
        try {
            licenseRepository.save(license);
        } catch (Exception ex) {
            log.error(String.format("error in createLicense with %s license and %s organizationId\nexception stacktrace:\n%s",
                    license,
                    organizationId,
                    Arrays.toString(ex.getStackTrace())));
            
            throw new IllegalArgumentException(
                    String.format(messages.getMessage(this.createErrorMessage, null, null),
                            license, organizationId)
            );
        }
        log.info("license %s created".formatted(license));
        return license.withComment(serviceConfig.getProperty());
    }
    
    @CircuitBreaker(name = serviceName)
    public License updateLicense(@NonNull License license, long organizationId) {
        if (license.getOrganizationId() == 0) {
            license.setOrganizationId(organizationId);
        }
        License savedLicense;
        try {
            savedLicense = licenseRepository.save(license);
        } catch (Exception ex) {
            log.error(String.format("error in updateLicense with %s license and %s organizationId\nexception stacktrace:\n%s",
                    license,
                    organizationId,
                    Arrays.toString(ex.getStackTrace())));
            throw new IllegalArgumentException(
                    String.format(messages.getMessage(this.updateErrorMessage, null, null),
                            license, organizationId));
        }
        log.info("license %s saved".formatted(license));
        return savedLicense.withComment(serviceConfig.getProperty());
    }
    
    @CircuitBreaker(name = serviceName)
    public String deleteLicense(UUID licenseId, long organizationId) {
        var license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        
        String responseMessage;
        if (license.isPresent()) {
            responseMessage = String.format(this.deleteLicenseNotFoundMessage, licenseId, organizationId);
        } else {
            try {
                licenseRepository.delete(license.get());
            } catch (Exception ex) {
                log.error(String.format("error in updateLicense with %s license and %s organizationId\nexception stacktrace:\n%s",
                        license,
                        organizationId,
                        Arrays.toString(ex.getStackTrace())));
                throw new IllegalArgumentException(
                        String.format(messages.getMessage(this.deleteErrorMessage, null, Locale.US),
                                license, organizationId));
            }
            responseMessage = String.format(this.deleteMessage, licenseId, organizationId);
        }
        log.info("license %s deleted".formatted(license));
        return responseMessage;
    }
}