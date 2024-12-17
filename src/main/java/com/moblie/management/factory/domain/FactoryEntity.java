package com.moblie.management.factory.domain;

import com.moblie.management.factory.dto.FactoryDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "factory")
@SQLDelete(sql = "UPDATE FACTORY set DELETED = true where FACTORY_ID = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FactoryEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long factoryId;

    @Column(unique = true, nullable = false)
    private String factoryName;

    private boolean deleted = false;

    @Builder
    public FactoryEntity(String factoryName) {
        this.factoryName = factoryName;
    }

    public void factoryUpdate(FactoryDto.factoryUpdate updateDto) {
        this.factoryName = updateDto.getFactoryName();
    }
}
