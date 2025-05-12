package com.moblie.management.local.store.controller;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.global.utils.PageCustom;
import com.moblie.management.local.member.service.MemberService;
import com.moblie.management.local.store.dto.StoreDto;
import com.moblie.management.global.utils.Response;
import com.moblie.management.local.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final MemberService memberService;
    private final StoreService storeService;

    //생성
    @PostMapping("/new")
    public ResponseEntity<Response> newStore(
            @Valid @RequestBody StoreDto.commonDto storeDto) {

        storeService.createStore(storeDto);

        return ResponseEntity.ok(new Response("생성 완료"));
    }

    //수정
    @PatchMapping("/update/{store_id}")
    public ResponseEntity<Response> updateStore(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "store_id") String storeId,
            @Valid @RequestBody StoreDto.commonDto updateDto) {

        isAccess(principalDetails.getEmail());
        storeService.updateStore(storeId, updateDto);

        return ResponseEntity.ok(new Response("수정 완료"));
    }

    //조회
    @GetMapping("/search")
    public PageCustom<StoreDto.storeSearchResponse> searchStore(
            @RequestParam("store_name") String storeName,
            @PageableDefault Pageable pageable) {

        StoreDto.storeCondition condition = new StoreDto.storeCondition(storeName);

        return storeService.searchStories(condition, pageable);
    }

    //삭제
    @DeleteMapping("/delete/{store_id}")
    public ResponseEntity<Response> deleteStore(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "store_id") String storeId) {

        isAccess(principalDetails.getEmail());
        storeService.deleteStore(principalDetails.getId(), storeId);

        return ResponseEntity.ok(new Response("삭제 완료"));
    }

    private void isAccess(String email) {
        if (memberService.isAccess(email)) {
            throw new CustomException(ErrorCode.ERROR_409, "권한이 없는 사용자 입니다.");
        }
    }
}
