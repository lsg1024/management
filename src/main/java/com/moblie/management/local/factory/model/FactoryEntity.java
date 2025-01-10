package com.moblie.management.local.factory.model;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.utils.BaseEntity;
import com.moblie.management.local.factory.dto.FactoryDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "factory")
@SQLDelete(sql = "UPDATE FACTORY set DELETED = true where FACTORY_ID = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FactoryEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long factoryId;

    @Column(unique = true, nullable = false)
    private String factoryName;

    private boolean deleted = false;

    @Builder(access = AccessLevel.PRIVATE)
    protected FactoryEntity(String factoryName) {
        this.factoryName = factoryName;
    }

    public static FactoryEntity create(String factoryName) {
        return FactoryEntity.builder()
                .factoryName(factoryName)
                .build();
    }

    public void factoryUpdate(FactoryDto.factory updateDto) {
        this.factoryName = updateDto.getFactoryName();
    }

    //테스트 전용
    public String getFactoryName() {
        return factoryName;
    }

    public void delete() {
        this.deleted = true;
    }

}
