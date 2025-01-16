package com.moblie.management.local.store.repository;

import com.moblie.management.local.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
    Store findByCreatedBy(String userId);
}
