package com.moblie.management.local.product.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ClassificationDto {

    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassificationList {
        private List<String> classifications;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class Version {
        private int version;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClassificationInfo {
        @NotEmpty(message = "존재하지 않은 카테고리 입니다.")
        private String classificationId;
        @NotEmpty(message = "존재하지 않은 카테고리 입니다.")
        private String classificationName;
    }
}
