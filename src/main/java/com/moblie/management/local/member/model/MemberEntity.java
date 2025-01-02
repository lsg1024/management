package com.moblie.management.local.member.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.UUID;

@Entity
@Getter
@Table(name = "members")
@SQLDelete(sql = "UPDATE MEMBERS set DELETED = true where USERID = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true) // oauth 데이터 정보
    private String username; // 사용자 플렛폼 정보
    @Column(unique = true)
    private String nickname; // 사용자 이름
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean deleted = false;

    @Builder
    public MemberEntity(Long userid, String email, String password, String username, String nickname, Role role) {
        this.userid = userid;
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
    }

    public boolean isUserNameExist() {
        return username != null;
    }
    public boolean isEmailExist() {
        return email != null;
    }
    public void updateUserNameAndEmail(String username, String email) {
        this.username = username;
        this.email = email;
    }
    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole() {
        this.role = Role.USER;
    }
    public void softDelete() {
        this.deleted = true;
        this.password = UUID.randomUUID().toString();
        this.username = null;
        this.nickname = "탈퇴한 사용자-" + UUID.randomUUID();
    }
}
