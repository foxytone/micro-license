package org.neat0n.licensingservice.license.controller;

import org.neat0n.licensingservice.aspects.annotations.GetLicenseErrorHandler;
import org.neat0n.licensingservice.aspects.annotations.LicenseConstrainCheck;
import org.neat0n.licensingservice.aspects.annotations.LicenseCreationCheck;
import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ) {
        License license = licenseService.getLicense(licenseId, organisationId);
        return ResponseEntity.ok(license);
    }

    @PutMapping
    @GetLicenseErrorHandler
    @LicenseConstrainCheck
    public ResponseEntity<License> updateLicense(
            @PathVariable("organizationId") long organizationId,
            @RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request, organizationId));
    }

    @PostMapping
    @LicenseCreationCheck
    public ResponseEntity<License> createLicense(
            @PathVariable("organizationId") long organizationId,
            @RequestBody License request) {
        return ResponseEntity.ok(licenseService.createLicense(request,
                organizationId));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<Boolean> deleteLicense(
            @PathVariable("organizationId") long organizationId,
            @PathVariable("licenseId") String licenseId) {
        System.out.println("delete worked");
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }
}
