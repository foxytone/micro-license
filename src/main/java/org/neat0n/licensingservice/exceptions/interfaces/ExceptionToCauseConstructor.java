package org.neat0n.licensingservice.exceptions.interfaces;

import org.neat0n.licensingservice.exceptions.LicenseServiceException;
import org.neat0n.licensingservice.exceptions.causes.ExceptionCause;
import org.springframework.stereotype.Component;

@Component
public interface ExceptionToCauseConstructor {
    public ExceptionCause construct(LicenseServiceException exception);
}
