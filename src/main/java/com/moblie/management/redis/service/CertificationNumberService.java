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

    public void createToken(String username, String certificationNumbers, String newPassword) {
        CertificationNumberToken certificationNumberToken = new CertificationNumberToken();
        certificationNumberToken.setUsername(username);
        certificationNumberToken.setRandomValue(certificationNumbers);
        certificationNumberToken.setPassword(newPassword);
        create(certificationNumberToken);
    }

    public Optional<CertificationNumberToken> getToken(String username) {
        return get(username);
    }

    public void deleteToken(String username) {
        delete(username);
    }

}
