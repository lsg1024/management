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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.moblie.management.global.utils.ExcelSheetUtil.getSheets;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FactoryService {

    private final FactoryRepository factoryRepository;

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
    public List<String> createManualProduct(FactoryDto.factoryInfo factoryInfo) {

        List<FactoryEntity> factories = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        extractedFactoryInfo(factoryInfo, factories, errors);

        factoryRepository.saveAll(factories);

        return errors;
    }

    @Cacheable(value = "factorySearch", key = "#condition.factoryName", cacheManager = "redisCacheManager")
    public PageCustom<FactoryDto.factoriesResponse> searchFactories(FactoryDto.factoryCondition condition, Pageable pageable) {
        return factoryRepository.searchFactories(condition, pageable);
    }

    //수정
    @DefaultRock(key = "#factoryId")
    public void updateFactory(String factoryId, FactoryDto.factoryUpdate updateDto) {
        FactoryEntity factory = factoryRepository.findById(Long.parseLong(factoryId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "업데이트에 실패하였스빈다."));
        factory.factoryUpdate(updateDto);
    }

    //삭제
    @Transactional
    public void deleteFactory(String factoryId) {
        FactoryEntity factory = factoryRepository.findById(Long.parseLong(factoryId))
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_404, "상품 삭제에 실패했습니다."));
        factoryRepository.delete(factory);
    }

    private void extractedFactoryInfo(FactoryDto.factoryInfo factoryInfo, List<FactoryEntity> factories, List<String> ErrorProduct) {
        for (FactoryDto.createFactory factory : factoryInfo.factories) {
            if (validateUnionProductName(factory, ErrorProduct)) {
                factories.add(FactoryEntity.create(factory.getFactoryName()));
            }
        }
    }

    //예외를 발생시 해당 데이터들을 모아서 반환
    private boolean validateUnionProductName(FactoryDto.createFactory factory, List<String> errors) {
        if (factoryRepository.existsByFactoryName(factory.getFactoryName())) {
            errors.add(factory.getFactoryName());
            return false;
        }
        return true;
    }
}
