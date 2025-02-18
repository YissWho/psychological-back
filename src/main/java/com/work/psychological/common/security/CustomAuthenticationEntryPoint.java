package com.work.psychological.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.psychological.common.api.ApiResult;
import com.work.psychological.common.api.ResultCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
/* 
 * 自定义认证入口点
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /* 
     * 对于未授权的请求，返回403 Forbidden
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        log.warn("Unauthorized request to: {}", request.getRequestURI());
        
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        
        ApiResult<?> result = ApiResult.failed(ResultCode.FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
} 