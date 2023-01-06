package org.neat0n.licensingservice.aspects;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.neat0n.licensingservice.exceptions.LicenseServiceException;
import org.neat0n.licensingservice.exceptions.causes.ExceptionCause;
import org.neat0n.licensingservice.exceptions.interfaces.ExceptionToCauseConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ThrowControlHandler {
    @Autowired
    private ExceptionToCauseConstructor causeConstructor;
    
    @Around("@annotation(org.neat0n.licensingservice.aspects.annotations.GetLicenseErrorHandler)")
    @SneakyThrows
    public Object errorController(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed(pjp.getArgs());
        } catch (LicenseServiceException ex) {
            return errorResponse(ex);
        }
    }
    
    private ResponseEntity<ExceptionCause> errorResponse(LicenseServiceException exception) {
        return ResponseEntity.internalServerError().body(causeConstructor.construct(exception));
        
    }
}
