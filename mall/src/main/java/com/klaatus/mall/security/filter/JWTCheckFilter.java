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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER_PREFIX = "Bearer ";
    private static final String OPTIONS_METHOD = "OPTIONS";
    private static final String API_MEMBER_PATH = "/api/member/";
    private static final String API_PRODUCTS_VIEW_PATH = "/api/products/view/";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing JWT Check Filter");

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith(AUTH_HEADER_PREFIX)) {

            try {
                String accessToken = authHeader.substring(AUTH_HEADER_PREFIX.length());
                Map<String, Object> claims = JWTUtil.validateToken(accessToken);

                log.info("JWT Claims: {}", claims);

                MemberDTO memberDTO = extractMemberDTOFromClaims(claims);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(memberDTO, memberDTO.getPassword(), memberDTO.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {
                handleException(response, e);
                return; // Stop further processing
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (OPTIONS_METHOD.equals(request.getMethod())) {
            return true;
        }

        String path = request.getRequestURI();
        log.info("JWT Check Filter path: {}", path);

        return path.startsWith(API_MEMBER_PATH) || path.startsWith(API_PRODUCTS_VIEW_PATH);
    }
    /**
     * 사용자 정보 추출
     * @param claims
     * @return
     */
    private MemberDTO extractMemberDTOFromClaims(Map<String, Object> claims) {

        String email = claims.get("email").toString();
        String password = claims.get("password").toString();
        String nickName = claims.get("nickName").toString();
        Boolean isSocial = (Boolean) claims.get("isSocial");
        List<String> roleNames = (List<String>) claims.get("roleNames");

        return new MemberDTO(email, password, nickName, isSocial, roleNames);
    }
    /**
     * 에러 처리
     * @param response
     * @param e
     * @throws IOException
     */
    private void handleException(HttpServletResponse response, Exception e) throws IOException {

        log.error("JWT Check Filter Exception: {}", e.getMessage());

        String errorMsg = new Gson().toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(errorMsg);
            out.flush();
        }
    }

}
