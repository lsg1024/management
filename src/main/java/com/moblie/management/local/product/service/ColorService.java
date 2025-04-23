package com.moblie.management.local.product.service;

import com.moblie.management.global.exception.CustomException;
import com.moblie.management.global.exception.ErrorCode;
import com.moblie.management.global.redis.service.VersionedService;
import com.moblie.management.local.product.dto.ColorDto;
import com.moblie.management.local.product.model.ColorEntity;
import com.moblie.management.local.product.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColorService extends VersionedService {

    private static final String VERSION_KEY = "material_color:version";

    private final RedisTemplate<String, Integer> versionTemplate;
    private final ColorRepository colorRepository;

    public int createColor(String name) {
        if (colorRepository.existsByColorName(name)) {
            throw new CustomException(ErrorCode.ERROR_409);
        }

        ColorEntity color = ColorEntity.builder()
                .colorName(name)
                .build();

        colorRepository.save(color);
        return increaseVersion();
    }

    public ColorDto.ColorList getColorList() {
        List<String> colors = colorRepository.findAll()
                .stream()
                .map(ColorEntity::getColorName)
                .collect(Collectors.toList());

        return new ColorDto.ColorList(colors);
    }

    public void deletedColor(String colorId, String colorName) {
        if (!colorRepository.existsByColorName(colorName)) {
            throw new CustomException(ErrorCode.ERROR_404, "존재하지 않는 값입니다.");
        }
        colorRepository.deleteById(Long.valueOf(colorId));
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
