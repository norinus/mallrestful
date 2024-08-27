package com.klaatus.mall.security.filter;

import com.google.gson.Gson;
import com.klaatus.mall.dto.MemberDTO;
import com.klaatus.mall.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("------------->JWT Check Filter<-----------");

        String authHeader = request.getHeader("Authorization");

        try {
            String accessToken = authHeader.substring(7);

            Map<String, Object> clams = JWTUtil.validateToken(accessToken);

            log.info("------------->JWT Check Clams: {}", clams);

            //AccessToken 에서 유저 정보 추출
            String email = clams.get("email").toString();
            String password = clams.get("password").toString();
            String nickName = clams.get("nickName").toString();
            Boolean isSocial = (Boolean) clams.get("isSocial");
            List<String> roleNames = (List<String>) clams.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(email, password, nickName, isSocial, roleNames);

            //권한 정보 생성
            List<SimpleGrantedAuthority> authorities = roleNames.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            //1. principal: 인증할 사용자 정보 memberDTO
            //2. credentials: 사용자의 자격 증명 password
            //3. authorities: 사용자의 권한 정보 authorities
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO, password, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {

            log.error("JWT Check Filter Exception: {}", e.getMessage());

            Gson gson = new Gson();

            String msg = gson.toJson(Map.of("error", e.getMessage()));
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(msg);
            out.flush();
            out.close();
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        //PreFlight 요청은 체크 하지 않는다.
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();

        log.info("JWT Check Filter path:{}", path);

        //필터에서 제외
        if (path.startsWith("/api/member/")) {
            return true;
        }

        //필터에서 제외
        if (path.startsWith("/api/products/view/")) {
            return true;
        }

        return false;
    }

}
