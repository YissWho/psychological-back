package com.work.psychological.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /* 
     * JWT令牌提供者
     */
    private final JwtTokenProvider jwtTokenProvider;

    /* 
     * 用户详情服务
     */
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            String requestUri = request.getRequestURI();
            String method = request.getMethod();
            
            log.info("Processing {} request to '{}', token: {}", 
                    method, requestUri, jwt != null ? "present" : "absent");

            // 对于登录和注册请求，直接放行
            if (requestUri.endsWith("/users/login") || requestUri.endsWith("/users/register")) {
                log.info("Allowing access to public endpoint: {}", requestUri);
                filterChain.doFilter(request, response);
                return;
            }

            // 如果JWT令牌存在，则验证令牌
            if (StringUtils.hasText(jwt)) {
                log.debug("Attempting to validate token");
                if (jwtTokenProvider.validateToken(jwt)) {
                    String username = jwtTokenProvider.getUsernameFromToken(jwt);
                    log.info("Token is valid for user: {}", username);
                    
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Authentication set for user: {} with authorities: {}", 
                            username, userDetails.getAuthorities());
                } else {
                    log.warn("Invalid JWT token for request to: {}", requestUri);
                }
            } else {
                log.info("No JWT token found in {} request to: {}", method, requestUri);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context for request to: {}", 
                    request.getRequestURI(), ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}