package com.moblie.management.local.factory.repository;

import com.moblie.management.local.factory.model.FactoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryRepository extends JpaRepository<FactoryEntity, Long>, FactoryRepositoryCustom {

    FactoryEntity findByFactoryName(String factoryName);
    boolean existsByFactoryName(String factoryName);
}
