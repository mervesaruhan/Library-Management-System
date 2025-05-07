package com.mervesaruhan.librarymanagementsystem.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import com.mervesaruhan.librarymanagementsystem.model.exception.GeneralErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        RestResponse<Object> errorResponse = RestResponse.errorAuth(GeneralErrorMessage.ACCESS_DENIED, HttpStatus.FORBIDDEN);
        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
