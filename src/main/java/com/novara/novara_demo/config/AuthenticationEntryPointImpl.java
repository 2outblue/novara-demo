package com.novara.novara_demo.config;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Map;

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
        Map<String, String> error = Map.of(
                "error", "Unauthorized",
                "message", authExceptionMessage.isBlank() ? "You don't have access to this resource" : authExceptionMessage
        );
        response.getWriter().write(gson.toJson(error));
    }
}
