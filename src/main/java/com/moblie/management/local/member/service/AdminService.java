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
    public void deleteMember(PrincipalDetails principalDetails, MemberDto.MemberEmail updateMemberInfo) {
        if (!principalDetails.getRole().equals("ROLE")) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }

        MemberEntity member = memberRepository.findByEmail(updateMemberInfo.getEmail());
        member.softDelete();
    }

}
