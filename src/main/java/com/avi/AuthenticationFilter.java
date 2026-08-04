package com.avi;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {
    private final TokenService tokenService;

    public AuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        //1. Allow login req to pass without authentication
        if(path.startsWith("/api/auth/")){
            chain.doFilter(request,response);
            return;
        }

        //2. agents endpoint
        if(path.startsWith("/api/agents/")){
            System.out.println("Tomcat Thread handling this request: " + Thread.currentThread().getName());
            String authHeader = httpRequest.getHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("{\"error\": \"Unauthorized: Missing Bearer Token\"}");
                return;
            }

            String token = authHeader.substring(7);

            if(!tokenService.isValid(token)){
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("{\"error\": \"Unauthorized: Missing Bearer Token\"}");
                return;
            }

            // 3. Token is valid, proceed to the next filter / controller
            chain.doFilter(request,response);
        }
    }
}
