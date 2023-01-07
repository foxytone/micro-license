package org.neat0n.licensingservice.license.controller;

import org.neat0n.licensingservice.aspects.annotations.GetLicenseErrorHandler;
import org.neat0n.licensingservice.exceptions.LicenseServiceException;
import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/organization/{organizationId}/license")
public class LicenseController {
    @Autowired
    private LicenseService licenseService;

    @GetMapping("/{licenseId}")
    @GetLicenseErrorHandler
    public ResponseEntity<License> getLicense(
            @PathVariable("organizationId") long organisationId,
            @PathVariable("licenseId") String licenseId
    ) throws LicenseServiceException {
        License license = licenseService.getLicense(licenseId, organisationId);
        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(
            @PathVariable("organizationId")
            long organizationId,
            @RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request, organizationId));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(
            @PathVariable("organizationId") long organizationId,
            @RequestBody License request,
            @RequestHeader(value = "Accept-Language", required = false)
            Locale locale) {
        return ResponseEntity.ok(licenseService.createLicense(request,
                organizationId));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(
            @PathVariable("organizationId") long organizationId,
            @PathVariable("licenseId") String licenseId) {
        System.out.println("delete worked");
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId,
                organizationId));
    }
}
