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
import java.util.Optional;
import java.util.UUID;

@Service
public class LicenseService {
    private final String serviceName = "licenseService";
    @Autowired
    LicenseRepository licenseRepository;
    @Autowired
    ServiceConfig serviceConfig;

    @CircuitBreaker(name = "licenseService")
    public License getLicense(String licenseUuid, long organizationId) throws LicenseServiceException {
        var license = licenseRepository
                .findByOrganizationIdAndUuid(organizationId, licenseUuid)
                .orElseThrow(() -> new LicenseServiceException(licenseUuid, organizationId, Errors.NO_LICENSE_FOUND));
        return license.withComment(serviceConfig.getProperty());
    }

    @CircuitBreaker(name = serviceName)
    public License createLicense(@NonNull License license, long organizationId) {
        Optional<License> possibleLicense = licenseRepository.findByOrganizationIdAndUuid(organizationId, license.getUuid());
        if (possibleLicense.isPresent()){
            throw new LicenseServiceException(license.getUuid(), organizationId, Errors.LICENSE_ALREADY_EXIST);
        }
        license.setUuid(null);
        license.setOrganizationId(organizationId);
        var savedLicense = licenseRepository.save(license);
        return savedLicense.withComment(serviceConfig.getProperty());
    }

    @CircuitBreaker(name = serviceName)
    public License updateLicense(@NonNull License license, long organizationId) {
        licenseRepository
                .findByOrganizationIdAndUuid(organizationId, license.getUuid())
                .orElseThrow(() -> new LicenseServiceException(license.getUuid(), organizationId, Errors.NO_LICENSE_FOUND));
        license.setOrganizationId(organizationId);
        var savedLicense = licenseRepository.save(license);
        return savedLicense.withComment(serviceConfig.getProperty());
    }

    @CircuitBreaker(name = serviceName)
    public boolean deleteLicense(String licenseUuid) {
        licenseRepository.deleteByUuid(licenseUuid);
        return true;
    }
}