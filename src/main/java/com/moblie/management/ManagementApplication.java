package com.moblie.management;

import com.moblie.management.global.jwt.dto.PrincipalDetails;
import com.moblie.management.global.redis.RedisClusterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
@EnableConfigurationProperties(RedisClusterProperties.class)
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> {
			// 인증된 사용자 정보를 가져옴
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.isAuthenticated()) {
				Object principal = authentication.getPrincipal();

				if (principal instanceof PrincipalDetails) {
					return Optional.of(((PrincipalDetails) principal).getId());
				}
			}

			return Optional.of(UUID.randomUUID().toString());
		};
	}

	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer customizer() {
		return p -> {
			p.setOneIndexedParameters(true);
		};
	}

}
