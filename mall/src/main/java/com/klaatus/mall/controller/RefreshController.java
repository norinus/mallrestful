package com.klaatus.mall.controller;

import com.klaatus.mall.utils.CustomJWTException;
import com.klaatus.mall.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RefreshController {

    @RequestMapping("/api/member/refresh")
    public Map<String, String> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {

        if (refreshToken == null) {
            throw new CustomJWTException("NULL REFRESH TOKEN");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("NULL AUTH HEADER");
        }

        String accessToken = authHeader.substring(7);

        if (!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken",refreshToken);
        }

        Map<String,Object> claims = JWTUtil.validateToken(refreshToken);

        //새 ACCESS 토큰 생성
        String newAccessToken = JWTUtil.generateToken(claims, 10);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) ? JWTUtil.generateToken(claims, 60*24):refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }
    /**
     * 토큰 만료 남은 시간 체크
     * @param exp
     * @return
     */
    private boolean checkTime(Integer exp) {
        Date expDate = new Date(exp * (1000));
        long gap = expDate.getTime() - System.currentTimeMillis();
        long leftMin = gap / (1000 * 60);
        return leftMin < 60;
    }

    /**
     * 토큰 만료 체크
     * @param token
     * @return
     */
    private boolean checkExpiredToken(String token) {
        try{
            JWTUtil.validateToken(token);
        }catch (CustomJWTException e) {
            if(e.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }

}
