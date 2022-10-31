package org.neat0n.licensingservice.utils;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();
    public UserContext getUserContext() {
        var context = userContext.get();
        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);
            setUserContext(context);
        }
        return userContext.get();
    }
    public void setUserContext(@NonNull UserContext context){
        userContext.set(context);
    }
    
    @Lookup
    protected UserContext createEmptyContext() {
        return null;
    }
}