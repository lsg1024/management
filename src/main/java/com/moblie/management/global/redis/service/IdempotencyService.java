package com.moblie.management.global.redis.service;

import com.moblie.management.global.redis.domain.IdempotencyToken;
import com.moblie.management.global.redis.repository.IdempotencyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class IdempotencyService extends RedisCrudService<IdempotencyToken, String> {

    private final IdempotencyRepository idempotencyRepository;
    protected IdempotencyService(IdempotencyRepository idempotencyRepository) {
        super(idempotencyRepository);
        this.idempotencyRepository = idempotencyRepository;
    }

    public void createToken(String email, String idempotencyValue) {
        IdempotencyToken idempotencyToken = new IdempotencyToken();
        idempotencyToken.setId(email);
        idempotencyToken.setIdempotencyValue(idempotencyValue);
        create(idempotencyToken);
    }

    public Optional<IdempotencyToken> getToken(String email) {
        return get(email);
    }

    public void deleteToken(String email) {
        delete(email);
    }

}
