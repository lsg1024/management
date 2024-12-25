package com.moblie.management.global.redis.repository;

import com.moblie.management.global.redis.domain.CertificationNumberToken;
import org.springframework.data.repository.CrudRepository;

public interface CertificationNumberRepository extends CrudRepository<CertificationNumberToken, String> {
}
