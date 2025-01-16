package com.moblie.management.local.factory.repository;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.factory.dto.FactoryDto;

import org.springframework.data.domain.Pageable;

public interface FactoryRepositoryCustom {
    PageCustom<FactoryDto.factoriesResponse> searchFactories(FactoryDto.factoryCondition factoryCondition, Pageable pageable);
    FactoryDto.find_factory findByFactoryId(String factoryId);
}
