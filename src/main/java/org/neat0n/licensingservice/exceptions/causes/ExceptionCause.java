package org.neat0n.licensingservice.exceptions.causes;

import lombok.NonNull;
import org.neat0n.licensingservice.exceptions.Errors;

import java.util.UUID;

public record ExceptionCause(String licenseUuid, long organizationId, @NonNull Errors code, String message) {
}
