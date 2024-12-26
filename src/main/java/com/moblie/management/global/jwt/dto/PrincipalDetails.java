package com.moblie.management.global.jwt.dto;
import com.moblie.management.local.member.domain.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PrincipalDetails implements UserDetails, OAuth2User {

    private final MemberEntity member;
    private Map<String, Object> attributes;

    public PrincipalDetails(MemberEntity member) {
        this.member = member;
    }

    public PrincipalDetails(MemberEntity member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    /** OAuth2 **/
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return member.getNickname();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().toString();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getNickname();
    }

    public String getEmail() {
        return member.getEmail();}
    public String getId() {
        return member.getUserid().toString();
    }

    public String getRole() {
        return member.getRole().toString();
    }


}
