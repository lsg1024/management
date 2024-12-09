package com.moblie.management.redis.service;

import com.moblie.management.redis.domain.CertificationNumberToken;
import com.moblie.management.redis.repository.CertificationNumberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CertificationNumberService extends RedisCrudService<CertificationNumberToken, String> {

    private final CertificationNumberRepository certificationNumberRepository;

    protected CertificationNumberService(CertificationNumberRepository certificationNumberRepository) {
        super(certificationNumberRepository);
        this.certificationNumberRepository = certificationNumberRepository;
    }

    public void createToken(String email, String certificationNumbers) {
        CertificationNumberToken certificationNumberToken = new CertificationNumberToken();
        certificationNumberToken.setEmail(email);
        certificationNumberToken.setRandomValue(certificationNumbers);
        create(certificationNumberToken);
    }

    public Optional<CertificationNumberToken> getToken(String email) {
        return get(email);
    }

    public void deleteToken(String username) {
        delete(username);
    }

}
