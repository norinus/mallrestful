package com.klaatus.mall.repository;

import com.klaatus.mall.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"memberRoleList"})
    Member findByEmail(String email);
}
