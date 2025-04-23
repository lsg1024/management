package com.moblie.management.local.product.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.redis.service.VersionedService;
import com.moblie.management.local.product.dto.MaterialDto;
import com.moblie.management.local.product.model.MaterialEntity;
import com.moblie.management.local.product.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialService extends VersionedService {

    private static final String VERSION_KEY = "material:version";

    private final RedisTemplate<String, Integer> versionTemplate;
    private final MaterialRepository materialRepository;

    public int createMaterial(String name) {
        if (materialRepository.existsByMaterialName(name)) {
            throw new CustomException(ErrorCode.ERROR_409);
        }

        MaterialEntity material = MaterialEntity.builder()
                .materialName(name)
                .build();

        materialRepository.save(material);
        return increaseVersion();
    }

    public MaterialDto.MaterialList getMaterialList() {
        List<String> names = materialRepository.findAll()
                .stream()
                .map(MaterialEntity::getMaterialName)
                .collect(Collectors.toList());

        return new MaterialDto.MaterialList(names);
    }

    public void deletedMaterial(String materialId, String materialName) {
        if (!materialRepository.existsByMaterialName(materialName)) {
            throw new CustomException(ErrorCode.ERROR_404, "존재하지 않은 값입니다.");
        }
        materialRepository.deleteById(Long.valueOf(materialId));
        increaseVersion();
    }

    @Override
    protected RedisTemplate<String, Integer> getVersionTemplate() {
        return versionTemplate;
    }

    @Override
    protected String getVersionKey() {
        return VERSION_KEY;
    }

}
