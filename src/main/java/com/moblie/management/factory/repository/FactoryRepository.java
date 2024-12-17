package com.moblie.management.factory.repository;

import com.moblie.management.factory.domain.FactoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryRepository extends JpaRepository<FactoryEntity, Long> {

    FactoryEntity findByFactoryName(String factoryName);
    boolean existsByFactoryName(String factoryName);
}
