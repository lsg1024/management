package com.moblie.management.local.member.repository;

import com.moblie.management.local.member.domain.MemberEntity;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepositoryCustom {

    @Transactional
    void deletePermanentlyDeletedMember();

    @Transactional
    MemberEntity findSoftDeleteMember(String email);

}
