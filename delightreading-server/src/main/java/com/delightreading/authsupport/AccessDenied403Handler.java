package com.delightreading.authsupport;

import com.delightreading.rest.BaseException;
import com.delightreading.rest.UnauthorizedException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

public class AccessDenied403Handler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        BaseException ex = new UnauthorizedException(request.getContextPath(), "");

        String exJson = objectMapper().writeValueAsString(ex.getExceptionBody());

        response.setStatus(ex.getExceptionBody().getHttpCode().value());
        response.setContentType(APPLICATION_JSON);
        response.getOutputStream().println(exJson);
    }

    ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        return objectMapper;
    }
}
