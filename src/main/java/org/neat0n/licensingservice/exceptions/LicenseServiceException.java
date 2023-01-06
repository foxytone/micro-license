package org.neat0n.licensingservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class LicenseServiceException extends Exception {
    private final UUID licenseId;
    private final long organizationId;
    private final Errors code;
}
