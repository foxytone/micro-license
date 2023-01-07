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
public class ConstraintCheck {

    @Around(value = "@annotation(org.neat0n.licensingservice.aspects.annotations.LicenseConstrainCheck) && args(licenseUuid, organizationId)", argNames = "pjp,licenseUuid,organizationId")
    @SneakyThrows
    public Object getCheck(@NonNull ProceedingJoinPoint pjp, @Nullable String licenseUuid, @Nullable Long organizationId) {
        UUIDCheck(licenseUuid, organizationId);
        organizationIdCheck(licenseUuid, organizationId);
        return pjp.proceed(pjp.getArgs());
    }

    @Around(value = "@annotation(org.neat0n.licensingservice.aspects.annotations.LicenseConstrainCheck) && args(organizationId, license)", argNames = "pjp,license,organizationId")
    @SneakyThrows
    public Object updateCheck(@NonNull ProceedingJoinPoint pjp, @Nullable License license, long organizationId) {
        licenseExistingCheck(license);
        UUIDCheck(license);
        licenseTypeCheck(license);
        descriptionCheck(license);
        productNameCheck(license);
        commentCheck(license);
        return pjp.proceed(pjp.getArgs());
    }

    @Around(value = "@annotation(org.neat0n.licensingservice.aspects.annotations.LicenseCreationCheck) && args(organizationId, license)", argNames = "pjp,license,organizationId")

    @SneakyThrows
    public Object createCheck(@NonNull ProceedingJoinPoint pjp, @Nullable License license, long organizationId) {
        licenseExistingCheck(license);
        licenseTypeCheck(license);
        descriptionCheck(license);
        productNameCheck(license);
        commentCheck(license);
        return pjp.proceed(pjp.getArgs());
    }

    private void UUIDCheck(String licenseUuid, Long organizationId) {
        if (licenseUuid == null) {
            throw constructException(licenseUuid, organizationId, Errors.EMPTY_LICENSE_UUID);
        }
    }

    private void UUIDCheck(@NonNull License license) {
        if (license.getUuid() == null) {
            throw constructException(license.getUuid(), license.getOrganizationId(), Errors.EMPTY_LICENSE_UUID);
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

    private void descriptionCheck(@NonNull License license) {
        if (license.getDescription() != null) {
            if (license.getDescription().length() <= 10 || license.getDescription().length() >= 120) {
                throw constructException(license.getUuid(), license.getOrganizationId(), Errors.INVALID_DESCRIPTION_LENGTH);
            }
        }
    }

    private void productNameCheck(@NonNull License license) {
        if (license.getProductName() != null) {
            if (license.getProductName().length() <= 3 || license.getProductName().length() >= 40) {
                throw constructException(license.getUuid(), license.getOrganizationId(), Errors.INVALID_PRODUCT_NAME_LENGTH);
            }
        }
    }

    private void commentCheck(@NonNull License license) {
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
