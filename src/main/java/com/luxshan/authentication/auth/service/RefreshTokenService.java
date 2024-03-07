package com.luxshan.authentication.auth.service;

import com.luxshan.authentication.auth.entity.RefreshToken;
import com.luxshan.authentication.auth.entity.User;
import com.luxshan.authentication.auth.repository.RefreshTokenRepository;
import com.luxshan.authentication.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByEmail(username) // findByEmail was findByUsername
                .orElseThrow(
                        () -> new UsernameNotFoundException("user not found with email " + username));
        RefreshToken refreshToken = user.getRefreshToken();

        if(refreshToken == null){
            long refreshTokenValidity = 30*1000; //before value 5*60*60*10000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();

            refreshTokenRepository.save(refreshToken);


        }
        return refreshToken;
    }
    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if(refToken.getExpirationTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refToken;
    }

}
