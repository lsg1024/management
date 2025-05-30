package com.moblie.management.local.factory.controller;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.global.utils.Response;
import com.moblie.management.local.factory.dto.FactoryDto;
import com.moblie.management.local.factory.service.FactoryService;
import com.moblie.management.local.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FactoryController {

    private final MemberService memberService;
    private final FactoryService factoryService;

    @PostMapping("/factories/new")
    public ResponseEntity<Response> createExcelFactory(
            @RequestParam MultipartFile file) throws IOException {

        List<String> errors = factoryService.createAutoFactory(file);

        log.info("errors {}", errors.iterator());

        return getResponse(errors);
    }

    @PostMapping("/factory/new")
    public ResponseEntity<Response> createManualProduct(
            @RequestBody FactoryDto.factoryInfo factories) {

        List<String> errors = factoryService.createManualFactory(factories);

        return getResponse(errors);
    }

    @GetMapping("/factory")
    public PageCustom<FactoryDto.factoriesResponse> searchFactory(
            @RequestParam("factory_name") String factoryName,
            @PageableDefault(size = 16) Pageable pageable) {

        FactoryDto.factoryCondition condition = new FactoryDto.factoryCondition(factoryName);

        return factoryService.searchFactories(condition, pageable);
    }

    @GetMapping("/factory/{factory_id}")
    public ResponseEntity<FactoryDto.find_factory> detailFactory(
            @PathVariable(name = "factory_id") String factoryId) {

        FactoryDto.find_factory factory = factoryService.detailFactory(factoryId);

        return ResponseEntity.ok(factory);
    }

    @PatchMapping("/factory/{factory_id}")
    public ResponseEntity<Response> updateFactory(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "factory_id") String factoryId,
            @RequestBody FactoryDto.factory updateFactory) {

        isAccess(principalDetails.getEmail());
        factoryService.updateFactory(factoryId, updateFactory);

        return ResponseEntity.ok(new Response("수정 완료"));
    }

    @DeleteMapping("/factory/{factory_id}")
    public ResponseEntity<Response> deleteFactory(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "factory_id") String factoryId) {

        isAccess(principalDetails.getEmail());
        factoryService.deleteFactory(factoryId);

        return ResponseEntity.ok(new Response("삭제 완료"));

    }

    private void isAccess(String email) {
        if (memberService.isAccess(email)) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }
    }

    private ResponseEntity<Response> getResponse(List<String> errors) {
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST, "저장 실패 목록", errors));
        }

        return ResponseEntity.ok(new Response("저장완료"));
    }

}
