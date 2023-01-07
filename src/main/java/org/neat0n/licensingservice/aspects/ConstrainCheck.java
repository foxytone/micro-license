package org.neat0n.licensingservice.aspects;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.neat0n.licensingservice.exceptions.Errors;
import org.neat0n.licensingservice.exceptions.LicenseServiceException;
import org.neat0n.licensingservice.license.model.License;
import org.neat0n.licensingservice.license.repo.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ConstrainCheck {
    @Autowired
    LicenseRepository licenseRepository;
    
    @Around(value = "@annotation(org.neat0n.licensingservice.aspects.annotations.LicenseConstrainCheck) && args(licenseUuid, organizationId)", argNames = "pjp,licenseUuid,organizationId")
    @SneakyThrows
    public Object check(@NonNull ProceedingJoinPoint pjp, @Nullable String licenseUuid, @Nullable Long organizationId) {
        licenseUUIDCheck(licenseUuid, organizationId);
        organizationIdCheck(licenseUuid, organizationId);
        licenseRepository
                .findByOrganizationIdAndUuid(organizationId, licenseUuid)
                .orElseThrow(() -> constructException(licenseUuid, organizationId, Errors.NO_LICENSE_FOUND));
        return pjp.proceed(pjp.getArgs());
    }
    
    
    @Around("@annotation(org.neat0n.licensingservice.aspects.annotations.LicenseConstrainCheck) && args(license)")
    @SneakyThrows
    public Object check(@NonNull ProceedingJoinPoint pjp, @Nullable License license) {

        licenseExistingCheck(license);
        licenseTypeCheck(license);
        licenseDescriptionCheck(license);
        licenseProductNameCheck(license);
        licenseCommentCheck(license);
        return check(pjp, license.getUuid(), license.getOrganizationId());
    }
    
    @Around(value = "@annotation(org.neat0n.licensingservice.aspects.annotations.LicenseConstrainCheck) && args(organizationId, license)", argNames = "pjp,license,organizationId")
    @SneakyThrows
    public Object check(@NonNull ProceedingJoinPoint pjp, @Nullable License license, long organizationId) {

        licenseExistingCheck(license);
        licenseTypeCheck(license);
        licenseDescriptionCheck(license);
        licenseProductNameCheck(license);
        licenseCommentCheck(license);
        return check(pjp, license.getUuid(), license.getOrganizationId());
    }
    
    private void licenseUUIDCheck(String licenseUuid, Long organizationId) {
        if (licenseUuid == null) {
            throw constructException(licenseUuid, organizationId, Errors.EMPTY_LICENSE_UUID);
        }
    }
    
    private void organizationIdCheck(String licenseUuid, Long organizationId) {
        if (organizationId == null) {
            throw constructException(licenseUuid, organizationId, Errors.EMPTY_ORGANIZATION_ID);
        }
    }
    
    private void licenseExistingCheck(@Nullable License license) {
        if (license == null) {
            throw constructException(null, null, Errors.NO_LICENSE_FOUND);
        }
    }
    
    private void licenseTypeCheck(@NonNull License license) {
        if (license.getLicenseType() != null) {
            if (license.getLicenseType().length() <= 3 || license.getLicenseType().length() >= 40) {
                throw constructException(license.getUuid(), license.getOrganizationId(), Errors.INVALID_LICENSE_TYPE_LENGTH);
            }
        }
    }
    
    private void licenseDescriptionCheck(@NonNull License license) {
        if (license.getDescription() != null) {
            if (license.getDescription().length() <= 10 || license.getDescription().length() >= 120) {
                throw constructException(license.getUuid(), license.getOrganizationId(), Errors.INVALID_DESCRIPTION_LENGTH);
            }
        }
    }
    
    private void licenseProductNameCheck(@NonNull License license) {
        if (license.getProductName() != null) {
            if (license.getProductName().length() <= 3 || license.getProductName().length() >= 40) {
                throw constructException(license.getUuid(), license.getOrganizationId(), Errors.INVALID_PRODUCT_NAME_LENGTH);
            }
        }
    }
    
    private void licenseCommentCheck(@NonNull License license) {
        if (license.getComment() != null) {
            if (license.getComment().length() >= 400) {
                throw constructException(license.getUuid(), license.getOrganizationId(), Errors.INVALID_COMMENT_LENGTH);
            }
        }
    }
    
    
    private @NonNull LicenseServiceException constructException(@Nullable String licenseUuid, @Nullable Long organizationId, @NonNull Errors code) {
        return new LicenseServiceException(
                licenseUuid,
                organizationId,
                code
        );
    }
}
