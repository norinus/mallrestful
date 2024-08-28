package com.klaatus.mall.dto;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

public class MemberDTO extends User {


    private static final long serialVersionUID = 1L;

    private String email;

    private String password;

    private String nickName;

    private Boolean isSocial;

    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(String email, String password, String nickName , Boolean isSocial, List<String> roleNames) {
        super(email, password, roleNames.stream().map(role-> new SimpleGrantedAuthority("ROLE_"+role)).toList());

        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.isSocial = isSocial;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims() {

        Map<String, Object> claims = new HashMap<>();

        claims.put("email", email);
        claims.put("password", password);
        claims.put("nickName", nickName);
        claims.put("isSocial", isSocial);
        claims.put("roleNames", roleNames);
        return claims;
    }
}
