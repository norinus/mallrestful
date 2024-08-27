package com.klaatus.mall.security;

import com.klaatus.mall.domain.Member;
import com.klaatus.mall.dto.MemberDTO;
import com.klaatus.mall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("사용자 로그인 처리");

        Member member = memberRepository.findByEmail(username);
        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        log.info("사용자 롤 정보 {}", member.getMemberRoleList().stream().map(Enum::name).toList());

        return new MemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.getNickName(),
                member.getIsSocial(),
                member.getMemberRoleList().stream().map(Enum::name).toList()
        );
    }
}
