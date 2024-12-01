package com.moblie.management.redis.service;

//import com.moblie.management.redis.dto.RefreshToken;
//import com.moblie.management.redis.repository.RefreshRepository;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

//@Service
//public class RedisTokenService implements PersistentTokenRepository {
//
//    private final RefreshRepository refreshRepository;
//
//    public RedisTokenService(RefreshRepository refreshRepository) {
//        this.refreshRepository = refreshRepository;
//    }
//
//    @Override
//    public void createNewToken(PersistentRememberMeToken token) {
//        RefreshToken refreshToken = new RefreshToken();
//        refreshToken.setSeries(token.getSeries());
//        refreshToken.setUsername(token.getUsername());
//        refreshToken.setTokenValue(token.getTokenValue());
//        refreshToken.setLastUsed(token.getDate().getTime());
//        refreshRepository.save(refreshToken);
//    }
//
//    @Override
//    public void updateToken(String series, String tokenValue, Date lastUsed) {
//        Optional<RefreshToken> optionalToken = refreshRepository.findById(series);
//        if (optionalToken.isPresent()) {
//            RefreshToken token = optionalToken.get();
//            token.setTokenValue(tokenValue);
//            token.setLastUsed(lastUsed.getTime());
//            refreshRepository.save(token);
//        }
//    }
//
//    @Override
//    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
//        Optional<RefreshToken> optionalToken = refreshRepository.findById(seriesId);
//        if (optionalToken.isPresent()) {
//            RefreshToken token = optionalToken.get();
//            return new PersistentRememberMeToken(
//                    token.getUsername(), token.getSeries(), token.getTokenValue(), new Date(token.getLastUsed())
//            );
//        }
//        return null;
//    }
//
//    @Override
//    public void removeUserTokens(String username) {
//        Optional<RefreshToken> optionalToken = refreshRepository.findByUsername(username);
//        optionalToken.ifPresent(refreshRepository::delete);
//    }
//}