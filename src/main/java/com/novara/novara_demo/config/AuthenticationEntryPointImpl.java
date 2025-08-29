package com.novara.novara_demo.config;

import com.google.gson.Gson;
import com.novara.novara_demo.model.exception.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final Gson gson = new Gson();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String authExceptionMessage = authException.getMessage();
        String errorCode = "";

        if (authException instanceof InsufficientAuthenticationException) {
            errorCode = "JWT_REQUIRED";
        } else if (authException instanceof InvalidBearerTokenException) {
            String exceptionMessage = authException.getMessage();
            if (exceptionMessage.contains("Jwt expired")) {
                errorCode = "JWT_EXPIRED";
            } else {
                errorCode = "JWT_INVALID";
            }
        }

        ApiError apiError = new ApiError(
                response.getStatus(),
                errorCode,
                authExceptionMessage.isBlank() ? "You don't have access to this resource" : authExceptionMessage
        );

        response.getWriter().write(gson.toJson(apiError));
    }
}
