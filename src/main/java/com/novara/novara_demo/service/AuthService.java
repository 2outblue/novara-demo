package com.novara.novara_demo.service;

import com.nimbusds.jose.util.Base64;
import com.novara.novara_demo.config.JWKAlgorithmImpl;
import com.novara.novara_demo.model.dto.NewTokenAuthenticationDTO;
import com.novara.novara_demo.model.entity.RefreshToken;
import com.novara.novara_demo.model.entity.User;
import com.novara.novara_demo.repository.RefreshTokenRepository;
import com.novara.novara_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.expiry-minutes}")
    private long expiryMinutes;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtEncoder jwtEncoder, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public NewTokenAuthenticationDTO generateNewTokenAuthentication(Authentication authentication) {
        String tokenHash = generateRefreshToken(authentication.getName());
        String jwt = generateJwtByUsername(authentication.getName());
        return new NewTokenAuthenticationDTO(tokenHash, jwt);
    }

    public NewTokenAuthenticationDTO validateRefreshToken(String rawRefreshToken) {
//        String sanitizedToken = sanitizeUuid(rawRefreshToken);
        if (rawRefreshToken == null || rawRefreshToken.isBlank() || !rawRefreshToken.contains(".")) {
            throw new IllegalArgumentException("Invalid refresh token format");
        }
        String[] tokenParams = rawRefreshToken.split("\\.");
        if (tokenParams.length != 2) {throw new IllegalArgumentException("Invalid refresh token format");}

        String publicKey = tokenParams[0];
        String rawToken = tokenParams[1];

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByLookup(publicKey)
                .orElseThrow(() -> new RuntimeException("No such refresh token"));
        if (refreshTokenEntity.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Expired refresh token");
        }
        if (!passwordEncoder.matches(rawToken, refreshTokenEntity.getToken())) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        User userEntity = userRepository.findByEmail(refreshTokenEntity.getUsername())
                .orElseThrow(() -> new RuntimeException("Could not find user associated with refresh token"));

        String newRefreshToken = generateRefreshToken(userEntity.getEmail());
        String newJwt = generateJwtByUsername(userEntity.getEmail());
        return new NewTokenAuthenticationDTO(newRefreshToken, newJwt);
    }

    private String generateJwtByUsername(String username) {
        var now = Instant.now();
        var expiresAt = now.plusSeconds(15 * 60);

        User userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Could not generate JWT. No associated user was found."));

        var roles = userEntity.getRoles();
        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(username)
                .claim("roles", roles)
                .build();

        var header = JwsHeader.with(new JWKAlgorithmImpl())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    private String generateRefreshToken(String username) {
        var now = Instant.now();
        var refreshTokenExpiry = now.plusSeconds(24 * 60 * 60);

        SecureRandom random = new SecureRandom();
        byte[] secret = new byte[32];
        random.nextBytes(secret);
        String encodedSecret = Base64.encode(secret).toString();
        String publicKey = Base64.encode(UUID.randomUUID().toString()).toString();
        RefreshToken refreshTokenEntity = new RefreshToken(passwordEncoder.encode(encodedSecret), publicKey);
        refreshTokenEntity.setUsername(username);
        refreshTokenEntity.setExpiryDate(refreshTokenExpiry);

        RefreshToken savedEntity = refreshTokenRepository.save(refreshTokenEntity);
        if (!passwordEncoder.matches(encodedSecret, savedEntity.getToken())) {
            throw new RuntimeException("Could not generate refresh token");
        }
        return publicKey + "." + encodedSecret;
    }

}
