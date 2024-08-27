package com.klaatus.mall.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JWTUtil {

    private static final String SECRET = "12345678901234567890123456789012345678901234567890";

    private static SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT Token 생성
     * @param claims
     * @param expirationMinutes
     * @return
     */
    public static String generateToken(Map<String, Object> claims, int expirationMinutes) {

      try {
          return Jwts.builder()
                  .setClaims(claims)
                  .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                  .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expirationMinutes).toInstant()))
                  .signWith(getKey())
                  .compact();
      }catch (RuntimeException e) {
          throw new CustomJWTException("generateToken Fail: " + e.getMessage());
      }
    }
    /**
     * JWT 토큰 유효성 검사
     * @param token
     * @return
     */
    public static Map<String, Object> validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new CustomJWTException("Invalid token: " + e.getMessage());
        }
    }
}
