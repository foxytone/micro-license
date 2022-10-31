package org.neat0n.licensingservice.filters;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.neat0n.licensingservice.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter
@Slf4j
public class PreUserContextFilter implements Filter {
    
    @Autowired
    UserContext userContext;
    
    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain){
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        userContext.setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID));
        userContext.setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        userContext.setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        userContext.setOrganizationId(httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));
    
        log.debug("UserContextFilter Correlation id: {}", userContext.getCorrelationId());
        log.info("webfilter worked");
        
        filterChain.doFilter(httpServletRequest, servletResponse);
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
    
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
