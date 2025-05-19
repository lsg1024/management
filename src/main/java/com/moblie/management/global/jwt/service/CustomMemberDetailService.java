package com.moblie.management.global.jwt.service;

import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.member.model.MemberEntity;
import com.moblie.management.local.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomMemberDetailService implements UserDetailsService {

    private final MemberRepository membersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity member = membersRepository.findByEmail(email);

        if (member == null) {
            log.info("사용자를 찾을 수 없습니다.");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        }

        if ("SECESSION".equals(String.valueOf(member.getRole()))) {
            log.info("탈퇴한 사용자 입니다: {}", email);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        }

        if ("WAIT".equals(String.valueOf(member.getRole()))) {
            log.info("미승인 사용자입니다: {}", email);
            throw new UsernameNotFoundException("미승인 사용자입니다");
        }

        log.info("사용자 인증 성공: {}, {}", email, member.getPassword());
        return new PrincipalDetails(member);
    }

}