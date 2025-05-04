package com.moblie.management.local.product.controller;

import com.moblie.management.global.utils.Response;
import com.moblie.management.local.product.dto.ClassificationDto;
import com.moblie.management.local.product.service.ClassificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClassificationController {

    private final ClassificationService classificationService;

    // 카테고리 추가
    @PostMapping("/classification")
    public ResponseEntity<ClassificationDto.Version> addClassification(
            @RequestParam("new_classification") String newClassification) {
        int version = classificationService.createClassification(newClassification);
        return ResponseEntity.ok(new ClassificationDto.Version(version));
    }

    // 카테고리 목록
    @GetMapping("/classification")
    public ResponseEntity<ClassificationDto.ClassificationList> classificationList() {
        return ResponseEntity.ok(classificationService.getClassificationList());
    }

    // 카테고리 삭제
    @DeleteMapping("/classification")
    public ResponseEntity<Response> deletedClassification(
            @Valid @RequestBody ClassificationDto.ClassificationInfo classificationInfo) {
        classificationService.deleteClassification(classificationInfo.getClassificationId(), classificationInfo.getClassificationName());

        return ResponseEntity.ok(new Response("삭제 완료"));
    }

    // 버전 체크 (만약 다르면 앱에서 목록 호출 후 새로 갱신 저장) Local < Server
    @GetMapping("/classification/version")
    public ResponseEntity<Integer> getVersion() {
        return ResponseEntity.ok(classificationService.getCurrentVersion());
    }

}
