package org.neat0n.licensingservice.aspects;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.neat0n.licensingservice.exceptions.LicenseServiceException;
import org.neat0n.licensingservice.exceptions.causes.ExceptionCause;
import org.neat0n.licensingservice.exceptions.interfaces.ExceptionToCauseConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1005000)
public class ThrowControlHandler {
    @Autowired
    private ExceptionToCauseConstructor causeConstructor;
    
    @Around("@annotation(org.neat0n.licensingservice.aspects.annotations.GetLicenseErrorHandler)")
    @SneakyThrows
    public @NonNull Object errorController(@NonNull ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed(pjp.getArgs());
        } catch (LicenseServiceException ex) {
            return errorResponse(ex);
            
        } catch (DataIntegrityViolationException ex) {
            var cause = ex.getCause().getCause().getMessage();
            return ResponseEntity.status(500).body(cause);
        }
    }
    
    private @NonNull ResponseEntity<ExceptionCause> errorResponse(@NonNull LicenseServiceException exception) {
        return ResponseEntity.internalServerError().body(causeConstructor.construct(exception));
    }
}
