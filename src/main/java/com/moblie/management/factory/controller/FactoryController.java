package com.moblie.management.factory.controller;

import com.moblie.management.factory.service.FactoryService;
import com.moblie.management.member.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FactoryController {

    private final FactoryService factoryService;

    @PostMapping("/factories/new")
    public ResponseEntity<Response> createExcelFactory(@RequestParam MultipartFile file) throws IOException {

        List<String> errors = factoryService.createAutoFactory(file);

        if (!errors.isEmpty()) {
            return ResponseEntity.ok(new Response("중복 데이터가 존재합니다."));
        }

        return ResponseEntity.ok(new Response("저장완료"));
    }

}
