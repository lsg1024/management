package com.moblie.management.local.product.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.local.product.dto.ClassificationDto;
import com.moblie.management.local.product.model.ClassificationEntity;
import com.moblie.management.local.product.repository.ClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ClassificationService {

    private static final String VERSION_KEY = "classification:version";

    private final RedisTemplate<String, Integer> versionTemplate;
    private final ClassificationRepository classificationRepository;

    // 카테고리 생성
    public int createClassification(String name) {
        if (classificationRepository.existsByClassificationName(name)) {
            throw new CustomException(ErrorCode.ERROR_409);
        }

        ClassificationEntity classification = ClassificationEntity.builder()
                .classificationName(name)
                .build();

        classificationRepository.save(classification);
        return increaseVersion();
    }

    // 카테고리 목록
    public ClassificationDto.ClassificationList getClassificationList() {

        List<String> names = classificationRepository.findAll()
                .stream()
                .map(ClassificationEntity::getClassificationName) // 이름만 추출
                .collect(Collectors.toList());

        return new ClassificationDto.ClassificationList(names);
    }

    // 카테고리 삭제
    public void deleteClassification(String classificationId, String classificationName) {
        if (!classificationRepository.existsByClassificationName(classificationName)) {
            throw new CustomException(ErrorCode.ERROR_404, "존재하지 않는 값입니다.");
        }
        classificationRepository.deleteById(Long.valueOf(classificationId));
        increaseVersion();
    }


    // 버전 증가
    private int increaseVersion() {
        Long newVersion = versionTemplate.opsForValue().increment(VERSION_KEY);
        return newVersion != null ? newVersion.intValue() : 0;
    }

    // 현재 버전 조회
    public int getCurrentVersion() {
        Integer version = versionTemplate    .opsForValue().get(VERSION_KEY);
        return version != null ? version : 0;
    }
}
