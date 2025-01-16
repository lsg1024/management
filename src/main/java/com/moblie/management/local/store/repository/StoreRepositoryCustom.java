package com.moblie.management.local.store.repository;

import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.store.dto.StoreDto;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {

    PageCustom<StoreDto.storeSearchResponse> searchStories(StoreDto.storeCondition condition, Pageable pageable);

}
