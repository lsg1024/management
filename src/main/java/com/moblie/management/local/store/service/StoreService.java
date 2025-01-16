package com.moblie.management.local.store.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.store.dto.StoreDto;
import com.moblie.management.local.store.model.Store;
import com.moblie.management.local.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    //생성 (수동)
    public void createStore(StoreDto.commonDto storeDto) {
        try {
            Store store = Store.create(storeDto);
            storeRepository.save(store);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.ERROR_500, e.getMessage());
        }
    }

    public void updateStore(String storeId, StoreDto.commonDto updateDto) {
        try {
            Store store = storeRepository.findById(Long.valueOf(storeId))
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

            store.update(updateDto);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.ERROR_500, e.getMessage());
        }
    }

    public PageCustom<StoreDto.storeSearchResponse> searchStories(StoreDto.storeCondition condition, Pageable pageable) {
        return storeRepository.searchStories(condition, pageable);
    }

    public void deleteStore(String userId, String storeId) {
        Store store = storeRepository.findById(Long.valueOf(storeId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

        store.delete(userId);
    }
}
