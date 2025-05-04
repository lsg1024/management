package com.moblie.management.local.product.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ColorDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColorList {
        private List<String> colors;
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
    public static class ColorInfo {
        @NotEmpty(message = "존재하지 않은 색상")
        private String colorId;
        @NotEmpty(message = "존재하지 않은 색상")
        private String colorName;
    }
}
