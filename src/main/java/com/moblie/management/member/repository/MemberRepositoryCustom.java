package com.moblie.management.member.repository;

import com.moblie.management.member.domain.MemberEntity;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepositoryCustom {

    @Transactional
    void deletePermanentlyDeletedMember();

    @Transactional
    MemberEntity findSoftDeleteMember(String email);

}
