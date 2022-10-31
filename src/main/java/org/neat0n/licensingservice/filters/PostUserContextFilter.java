package org.neat0n.licensingservice.filters;

import lombok.extern.slf4j.Slf4j;
import org.neat0n.licensingservice.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
@Slf4j
public class PostUserContextFilter implements Filter {
    @Autowired
    UserContext userContext;
    
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
        httpServletResponse.setHeader(UserContext.CORRELATION_ID, userContext.getCorrelationId());
        httpServletResponse.setHeader(UserContext.USER_ID, userContext.getUserId());
        httpServletResponse.setHeader(UserContext.AUTH_TOKEN, userContext.getAuthToken());
        httpServletResponse.setHeader(UserContext.ORGANIZATION_ID, userContext.getOrganizationId());
    
        log.debug("UserContextOutFilter Correlation id: {}", userContext.getCorrelationId());
        log.info("UserContextOutFilter worked");
    
        filterChain.doFilter(servletRequest, httpServletResponse);
    }
}
