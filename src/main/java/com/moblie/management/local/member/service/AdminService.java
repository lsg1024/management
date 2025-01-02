package com.moblie.management.local.member.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.local.member.domain.MemberEntity;
import com.moblie.management.local.member.dto.MemberDto;
import com.moblie.management.local.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateMemberRole(MemberDto.MemberEmail updateMemberInfo) {
        MemberEntity member = memberRepository.findByEmail(updateMemberInfo.getEmail());
        member.updateRole();
    }

    @Transactional
    public void deleteMember(MemberDto.MemberEmail updateMemberInfo) {
        MemberEntity member = memberRepository.findByEmail(updateMemberInfo.getEmail());
        member.softDelete();
    }

}
