package com.klaatus.mall.repository;

import com.klaatus.mall.domain.Member;
import com.klaatus.mall.domain.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Slf4j
public class MemberRepositoryTest {


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    void testInsert(){

        for(int i=1; i<=10 ;i++ ){

            Member member = Member.builder().email("user"+String.valueOf(i)+"@test.com").password(passwordEncoder.encode("1234")).nickName("user"+String.valueOf(i)).build();

            member.addRole(MemberRole.USER);

            if(i>=5){
                member.addRole(MemberRole.MANAGER);
            }

            if(i>=8){
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        }


    }

    @Test
    void testRead(){

        String email = "user1@test.com";
        Member member = memberRepository.findByEmail(email);

        log.info(member.toString());
    }

}
