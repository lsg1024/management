package com.moblie.management.local.factory.repository;

import com.moblie.management.local.factory.domain.FactoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryRepository extends JpaRepository<FactoryEntity, Long> {

    FactoryEntity findByFactoryName(String factoryName);
    boolean existsByFactoryName(String factoryName);
}
