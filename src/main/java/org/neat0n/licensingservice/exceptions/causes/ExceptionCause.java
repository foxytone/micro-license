package org.neat0n.licensingservice.exceptions.causes;

import org.neat0n.licensingservice.exceptions.Errors;

public record ExceptionCause(String licenseUuid, long organizationId, Errors reason, int code, String message) {
}
