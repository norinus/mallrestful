package com.klaatus.mall.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTUtil {

    private static final String SECRET = "12345678901234567890123456789012345678901234567890";

    public static String generateToken(Map<String, Object> value, int min) {

        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(JWTUtil.SECRET.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return Jwts.builder().setHeader(Map.of("typ", "JWT")).setClaims(value).setIssuedAt(Date.from(ZonedDateTime.now().toInstant())).setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())).signWith(key).compact();
    }

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claim =null;

        try {
            SecretKey key = Keys.hmacShaKeyFor(JWTUtil.SECRET.getBytes(StandardCharsets.UTF_8));
            claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        } catch (JwtException e) {
            throw new CustomJWTException(e.getMessage());
        }

        return claim;
    }
}
