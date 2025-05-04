package com.moblie.management.local.product.repository;

import com.moblie.management.local.product.model.ClassificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassificationRepository extends JpaRepository<ClassificationEntity, Long> {
    ClassificationEntity findByClassificationName(String classificationName);
    boolean existsByClassificationName(String classificationName);
}
