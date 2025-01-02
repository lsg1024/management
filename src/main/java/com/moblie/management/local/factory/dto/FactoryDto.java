package com.moblie.management.local.factory.dto;

import com.moblie.management.local.factory.model.FactoryEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FactoryDto {

    public static class factoryInfo {
        @Valid @NotNull
        public List<createFactory> factories;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class createFactory {
        @NotEmpty(message = "공장 이름을 입력해주세요.")
        private String factoryName;

        public FactoryEntity toEntity() {
            return FactoryEntity.builder()
                    .factoryName(factoryName)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class factoryUpdate {
        @NotEmpty(message = "공장 이름을 입력해주세요.")
        private String factoryName;
    }
}
