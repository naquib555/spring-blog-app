package com.blog.app.security;

import com.blog.app.security.domain.UserPrincipal;
import com.blog.app.service.impl.RedisUtils;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${app.authentication.jwtSecret}")
    private String jwtSecret;

    @Value("${app.authentication.jwtExpirationInMs}")
    private int jwtExpirationInMs;



    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String token =  Jwts.builder()
                .setSubject(userPrincipal.getUserId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        RedisUtils.hset(token, "userId", userPrincipal.getUserId().toString());
        RedisUtils.hset(token, "expiration", String.valueOf(expiryDate.getTime()));

        return token;
    }


    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Date getExpirationFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public static void addBlackList(String token) {
        if (StringUtils.isNotEmpty(token)) {
            RedisUtils.hset("blackList", token, String.valueOf(new Date().getTime()));
        }
    }


    public static void deleteRedisToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            RedisUtils.deleteKey(token);
        }
    }


    public static boolean isBlackList(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hasKey("blackList", token);
        }
        return false;
    }


    public static boolean isExpiration(String expiration) {
        Date expiryDate = new Date(Long.valueOf(expiration));
        Date now = new Date();
        if (now.compareTo(expiryDate) > 0) {
            return true;
        }
        return false;
    }


    public static boolean isValid(String refreshTime) {
        LocalDateTime validTime = LocalDateTime.parse(refreshTime, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.compareTo(validTime) > 0) {
            return false;
        }
        return true;
    }


    public static boolean hasToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hasKey(token);
        }
        return false;
    }


    public static String getExpirationByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hget(token, "expiration").toString();
        }
        return null;
    }


    public static String getRefreshTimeByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hget(token, "refreshTime").toString();
        }
        return null;
    }


    public static String getUserIdByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hget(token, "userId").toString();
        }
        return null;
    }


    public static String getIpByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hget(token, "ip").toString();
        }
        return null;
    }

    public static String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
