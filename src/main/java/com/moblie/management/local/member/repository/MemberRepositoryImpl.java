package com.moblie.management.local.member.repository;

import com.moblie.management.local.member.domain.MemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.moblie.management.member.domain.QMemberEntity.*;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory query;

    public MemberRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public void deletePermanentlyDeletedMember() {
        query
                .delete(memberEntity)
                .where(memberEntity.deleted.eq(true))
                .execute();

    }

    @Override
    public MemberEntity findSoftDeleteMember(String email) {
        return query
                .selectFrom(memberEntity)
                .where(memberEntity.deleted.eq(true)
                        .and(memberEntity.email.eq(email)))
                .fetchOne();
    }
}
