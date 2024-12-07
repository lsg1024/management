package com.moblie.management.redis.repository;

import com.moblie.management.redis.domain.CertificationNumberToken;
import org.springframework.data.repository.CrudRepository;

public interface CertificationNumberRepository extends CrudRepository<CertificationNumberToken, String> {
}
