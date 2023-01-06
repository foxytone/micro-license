package org.neat0n.licensingservice.exceptions;

import lombok.NonNull;
import org.neat0n.licensingservice.exceptions.causes.ExceptionCause;
import org.neat0n.licensingservice.exceptions.interfaces.ExceptionToCauseConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Primary
public class ExceptionToCauseConstructorImpl implements ExceptionToCauseConstructor {
    private final static String messagePrefix = "license.error.";
    @Autowired
    MessageSource messageSource;
    
    @Override
    public @NonNull ExceptionCause construct(@NonNull LicenseServiceException exception) {
        return new ExceptionCause(
                exception.getLicenseId(),
                exception.getOrganizationId(),
                exception.getCode(),
                messageSource.getMessage(
                        messagePrefix + exception.getCode(),
                        new Object[]{exception.getLicenseId(),
                                exception.getOrganizationId()},
                        Locale.US));
    }
}
