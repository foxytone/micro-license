package org.neat0n.licensingservice.license.controller;

import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("v1/organization/{organizationId}/license")
public class LicenseController {
    @Autowired
    private LicenseService licenseService;
    
    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(
            @PathVariable("organizationId") String organisationId,
            @PathVariable("licenseId") String licenseId
    ) {
        License license = licenseService.getLicense(licenseId, organisationId);
        license.add(linkTo(methodOn(LicenseController.class)
                        .getLicense(organisationId, licenseId))
                        .withSelfRel(),
                linkTo(methodOn(LicenseController.class)
                        .createLicense(organisationId, license, null))
                        .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .updateLicense(organisationId, license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
                        .deleteLicense(organisationId, license.getLicenseId())
                ).withRel("deleteLicense")
        );
        return ResponseEntity.ok(license);
    }
    
    @PutMapping
    public ResponseEntity<String> updateLicense(
            @PathVariable("organizationId")
            String organizationId,
            @RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request,
                organizationId));
    }
    
    @PostMapping
    public ResponseEntity<String> createLicense(
            @PathVariable("organizationId") String organizationId,
            @RequestBody License request,
            @RequestHeader(value = "Accept-Language", required = false)
            Locale locale) {
        return ResponseEntity.ok(licenseService.createLicense(request,
                organizationId, locale));
    }
    
    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        System.out.println("delete worked");
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId,
                organizationId));
    }
}
