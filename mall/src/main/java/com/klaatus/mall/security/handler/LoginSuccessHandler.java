package com.klaatus.mall.security.handler;

import com.google.gson.Gson;
import com.klaatus.mall.dto.MemberDTO;
import com.klaatus.mall.utils.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 핸들러");
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
        Map<String, Object> claims =memberDTO.getClaims();

        //엑서스 토큰
        String accessToken = JWTUtil.generateToken(claims,10);
        
        //리프레시 토큰
        String refreshToken = JWTUtil.generateToken(claims,60*24); //하루

        claims.put("accessToken",accessToken);
        claims.put("refreshToken",refreshToken);

        Gson gson = new Gson();
        String json = gson.toJson(claims);

        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();

        //api/main


    }
}
