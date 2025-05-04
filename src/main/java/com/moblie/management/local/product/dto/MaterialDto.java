package com.moblie.management.local.product.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MaterialDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialList {
        private List<String> materials;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Version {
        private int version;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialInfo {
        @NotEmpty(message = "존재하지 않은 재질")
        private String materialId;
        @NotEmpty(message = "존재하지 않은 재질")
        private String materialName;
    }
}
