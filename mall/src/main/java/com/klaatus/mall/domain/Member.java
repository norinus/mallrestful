package com.klaatus.mall.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member {

    @Id
    private String email;

    private String password;

    private String nickName;

    @Builder.Default
    private Boolean isSocial =false;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void addRole(MemberRole role) {
        memberRoleList.add(role);
    }

    public void clearRoles() {
        memberRoleList.clear();
    }

    public void changeNickName(String newNickName) {
        this.nickName = newNickName;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeSocial(Boolean newSocial) {
        this.isSocial = newSocial;
    }
}
