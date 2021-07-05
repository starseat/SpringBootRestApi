package com.rest.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 온전한 JWT 가 전달되지 않을 경우 토큰 인증 처리 자체가 불가능하기 때문에
 * 토큰 검증 단에서 예외 잡음.
 *
 * JWT 토큰 없이 API 호출하였을 경우.
 * 형식에 맞지 않거나 만료된 JWT 토큰으로 API 호출한 경우
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect("/exception/entrypoint");
    }
}