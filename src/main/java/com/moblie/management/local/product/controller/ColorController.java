package com.moblie.management.local.product.controller;

import com.moblie.management.global.utils.Response;
import com.moblie.management.local.product.dto.ColorDto;
import com.moblie.management.local.product.service.ColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @PostMapping("/material_color")
    public ResponseEntity<ColorDto.Version> addColor(
            @RequestParam("new_material_color") String newMaterialColor) {
        int version = colorService.createColor(newMaterialColor);
        return ResponseEntity.ok(new ColorDto.Version(version));
    }

    @GetMapping("/material_color")
    public ResponseEntity<ColorDto.ColorList> colorList() {
        return ResponseEntity.ok(colorService.getColorList());
    }

    @DeleteMapping("/material_color")
    public ResponseEntity<Response> deletedColor(
            @Valid @RequestBody ColorDto.ColorInfo colorInfo) {
        colorService.deletedColor(colorInfo.getColorId(), colorInfo.getColorName());
        return ResponseEntity.ok(new Response("삭제 완료"));
    }

    @GetMapping("/material_color/version")
    public ResponseEntity<Integer> getVersion() {
        return ResponseEntity.ok(colorService.getCurrentVersion());
    }
}
