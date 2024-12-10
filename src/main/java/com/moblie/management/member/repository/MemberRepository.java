package com.moblie.management.member.repository;

import com.moblie.management.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByUsernameAndEmail(String username, String email);
    MemberEntity findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    boolean existsByPassword(String password);

}
