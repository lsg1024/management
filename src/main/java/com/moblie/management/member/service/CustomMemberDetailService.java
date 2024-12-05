package com.moblie.management.member.service;

import com.moblie.management.jwt.dto.PrincipalDetails;
import com.moblie.management.member.domain.MemberEntity;
import com.moblie.management.member.repository.MemberRepository;
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
        MemberEntity members = membersRepository.findByEmail(email);

        log.info("loadUserByUsername");

        if (members == null) {
            log.info("사용자를 찾을 수 없습니다.");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }

        log.info("members.getRole(): {}", members.getRole());

        if ("WAIT".equals(String.valueOf(members.getRole()))) {
            log.info("미승인 사용자입니다: {}", email);
            throw new UsernameNotFoundException("미승인 사용자입니다: " + email);
        }

        log.info("사용자 인증 성공: {}, {}", email, members.getPassword());
        return new PrincipalDetails(members);
    }

}