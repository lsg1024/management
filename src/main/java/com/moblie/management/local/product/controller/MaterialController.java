package com.moblie.management.local.product.controller;

import com.moblie.management.global.utils.Response;
import com.moblie.management.local.product.dto.MaterialDto;
import com.moblie.management.local.product.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping("/material")
    public ResponseEntity<MaterialDto.Version> addMaterial(
            @RequestParam("new_material") String newMaterial) {
        int version = materialService.createMaterial(newMaterial);
        return ResponseEntity.ok(new MaterialDto.Version(version));
    }

    @GetMapping("/material")
    public ResponseEntity<MaterialDto.MaterialList> materialList() {
        return ResponseEntity.ok(materialService.getMaterialList());
    }

    @DeleteMapping("/material")
    public ResponseEntity<Response> deletedMaterial(
            @Valid @RequestBody MaterialDto.MaterialInfo materialInfo) {
        materialService.deletedMaterial(materialInfo.getMaterialId(), materialInfo.getMaterialName());
        return ResponseEntity.ok(new Response("삭제 완료"));
    }

    @GetMapping("/material/version")
    public ResponseEntity<Integer> getVersion() {
        return ResponseEntity.ok(materialService.getCurrentVersion());
    }

}
