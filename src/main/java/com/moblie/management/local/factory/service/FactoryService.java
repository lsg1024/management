package com.moblie.management.local.factory.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.redis.rock.DefaultRock;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.factory.model.FactoryEntity;
import com.moblie.management.local.factory.dto.FactoryDto;
import com.moblie.management.local.factory.repository.FactoryRepository;
import com.moblie.management.local.factory.excel.ExcelFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.moblie.management.global.utils.ExcelSheetUtil.getSheets;

@Slf4j
@Service
@Transactional(readOnly = true)
public class FactoryService {

    private final FactoryRepository factoryRepository;
    private final CacheManager cacheManager;

    public FactoryService(FactoryRepository factoryRepository, CacheManager cacheManager) {
        this.factoryRepository = factoryRepository;
        this.cacheManager = cacheManager;
    }

    //자동생성
    @Transactional
    public List<String> createAutoFactory(MultipartFile file) throws IOException {
        Workbook workbook = getSheets(file);

        Sheet workSheet = workbook.getSheetAt(0);

        FactoryDto.factoryInfo factoryInfo = new FactoryDto.factoryInfo();

        ExcelFactory.formatFactoryExcelData(workSheet, factoryInfo);

        List<FactoryEntity> factories = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        extractedFactoryInfo(factoryInfo, factories, errors);

        factoryRepository.saveAll(factories);

        return errors;
    }

    //수동생성
    @Transactional
    public List<String> createManualFactory(FactoryDto.factoryInfo factoryInfo) {

        List<FactoryEntity> factories = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        extractedFactoryInfo(factoryInfo, factories, errors);

        factoryRepository.saveAll(factories);

        return errors;
    }

    //페이지 조회
    @Cacheable(value = "factorySearch", key = "#condition.factoryName + #pageable.pageNumber", cacheManager = "redisCacheManager")
    public PageCustom<FactoryDto.factoriesResponse> searchFactories(FactoryDto.factoryCondition condition, Pageable pageable) {
        return factoryRepository.searchFactories(condition, pageable);
    }

    //상세 조회
    public FactoryDto.find_factory detailFactory(String factoryId) {
        return factoryRepository.findByFactoryId(factoryId);
    }

    //수정
    @DefaultRock(key = "#factoryId")
    public void updateFactory(String factoryId, FactoryDto.factory updateDto) {
        FactoryEntity factory = factoryRepository.findById(Long.parseLong(factoryId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404));

        if (factoryRepository.existsByFactoryName(updateDto.getFactoryName())) {
            throw new CustomException(ErrorCode.ERROR_400, "동일한 공장 존재");
        }
        factory.factoryUpdate(updateDto);
    }

    //삭제
    @DefaultRock(key = "#factoryId")
    public void deleteFactory(String factoryId) {
        FactoryEntity factory = factoryRepository.findById(Long.parseLong(factoryId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "상품 삭제 실패"));
        factoryRepository.delete(factory);
    }

    private void extractedFactoryInfo(FactoryDto.factoryInfo factoryInfo, List<FactoryEntity> factories, List<String> ErrorFactories) {
        for (FactoryDto.factory factory : factoryInfo.factories) {
            if (validateUnionFactoryName(factory, ErrorFactories)) {
                factories.add(FactoryEntity.create(factory.getFactoryName()));
            }
        }
    }

    //예외를 발생시 해당 데이터들을 모아서 반환
    private boolean validateUnionFactoryName(FactoryDto.factory factory, List<String> errors) {
        if (factoryRepository.existsByFactoryName(factory.getFactoryName())) {
            errors.add(factory.getFactoryName());
            return false;
        }
        return true;
    }
}
