package org.neat0n.licensingservice.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Getter
@Scope("prototype")
public final class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN = "tmx-auth-token";
    public static final String USER_ID = "tmx-user-id";
    public static final String ORGANIZATION_ID = "tmx-organization-id";
    private static UserContextHolder contextHolder;
    
    public UserContext(@Autowired UserContextHolder holder) {
        if (contextHolder == null) {
            contextHolder = holder;
        }
    }
    
    private String correlationId = "";
    private String authToken = "";
    private String userId = "";
    private String organizationId = "";
    
    
    public void setCorrelationId(String correlationId) {
        contextHolder.getUserContext().setThreadSafeCorrelationId(correlationId);
    }
    
    public void setAuthToken(String authToken) {
        contextHolder.getUserContext().setThreadSafeAuthToken(authToken);
    }
    
    public void setUserId(String userId) {
        contextHolder.getUserContext().setThreadSafeUserId(userId);
    }
    
    public void setOrganizationId(String organizationId) {
        contextHolder.getUserContext().setThreadSafeOrganizationId(organizationId);
    }
    
    
    private void setThreadSafeCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
    
    private void setThreadSafeAuthToken(String authToken) {
        this.authToken = authToken;
    }
    
    private void setThreadSafeUserId(String userId) {
        this.userId = userId;
    }
    
    private void setThreadSafeOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public String getCorrelationId() {
        return contextHolder.getUserContext().getThreadSafeCorrelationId();
    }
    
    public String getAuthToken() {
        return contextHolder.getUserContext().getThreadSafeAuthToken();
    }
    
    public String getUserId() {
        return contextHolder.getUserContext().getThreadSafeUserId();
    }
    
    public String getOrganizationId() {
        return contextHolder.getUserContext().getThreadSafeOrganizationId();
    }
    
    
    private String getThreadSafeCorrelationId() {
        return this.correlationId;
    }
    
    private String getThreadSafeAuthToken() {
        return this.authToken;
    }
    
    private String getThreadSafeUserId() {
        return this.userId;
    }
    
    private String getThreadSafeOrganizationId() {
        return this.organizationId;
    }
    
}
