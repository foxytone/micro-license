package org.neat0n.licensingservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LicenseServiceException extends Exception {
    private final String licenseUuid;
    private final long organizationId;
    private final Errors code;
}
