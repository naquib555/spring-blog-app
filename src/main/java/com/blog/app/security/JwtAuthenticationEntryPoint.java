package com.blog.app.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Value("${app.authentication.jwtSecret}")
    private String jwtSecret;

    @Value("${app.authentication.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());

        String jwt = null;
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwt = bearerToken.substring(7, bearerToken.length());
        }

        HttpSession session = httpServletRequest.getSession();

        String message = e.getMessage();
        if(jwt != null) {
            try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            } catch (ExpiredJwtException ex) {
                message = "Token has been Expired or Revoked";
            } catch (Exception ex) {

            }
        }
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
