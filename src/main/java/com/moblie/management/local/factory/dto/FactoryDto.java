package com.moblie.management.local.factory.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class FactoryDto {

    public static class factoryInfo {
        @Valid @NotNull
        public List<factory> factories;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class factory {
        @NotEmpty(message = "공장 이름을 입력해주세요.")
        private String factoryName;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class find_factory {
        private Long factoryId;
        private String factoryName;

        @QueryProjection
        public find_factory(Long factoryId, String factoryName) {
            this.factoryId = factoryId;
            this.factoryName = factoryName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class factoryCondition {
        private String factoryName;

        public factoryCondition(String factoryName) {
            this.factoryName = factoryName;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class factoriesResponse {
        private Long factoryId;
        private String factoryName;

        @QueryProjection
        public factoriesResponse(Long factoryId, String factoryName) {
            this.factoryId = factoryId;
            this.factoryName = factoryName;
        }
    }
}
