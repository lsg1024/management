package com.moblie.management.jwt.dto;

import lombok.Getter;

public interface OAuth2Response {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
