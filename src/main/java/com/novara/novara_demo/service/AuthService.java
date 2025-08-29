package com.novara.novara_demo.service;

import com.nimbusds.jose.util.Base64;
import com.novara.novara_demo.config.JWKAlgorithmImpl;
import com.novara.novara_demo.model.dto.NewTokenAuthenticationDTO;
import com.novara.novara_demo.model.entity.RefreshToken;
import com.novara.novara_demo.model.entity.User;
import com.novara.novara_demo.model.exception.ExpiredRefreshTokenException;
import com.novara.novara_demo.model.exception.RefreshTokenException;
import com.novara.novara_demo.model.exception.UserNotFoundException;
import com.novara.novara_demo.repository.RefreshTokenRepository;
import com.novara.novara_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.expiry-minutes}")
    private long jwtExpiryMinutes;

    @Value("${app.jwt.refresh-expiry-hours}")
    private long refreshExpiryHours;

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
        String tokenHash = generateNewRefreshToken(authentication.getName());
        String jwt = generateJwtByUsername(authentication.getName());
        return new NewTokenAuthenticationDTO(tokenHash, jwt);
    }

    public NewTokenAuthenticationDTO validateRefreshToken(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank() || !rawRefreshToken.contains(".")) {
            throw new RefreshTokenException();
        }
        String[] tokenParams = rawRefreshToken.split("\\.");
        if (tokenParams.length != 2) {throw new RefreshTokenException();}

        String publicKey = tokenParams[0];
        String rawToken = tokenParams[1];

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByLookup(publicKey)
                .orElseThrow(() -> new RefreshTokenException());

        if (refreshTokenEntity.isRevoked()) {
            invalidateTokenFamily(refreshTokenEntity.getFamilyId());
            throw new RefreshTokenException();
        }

        if (!passwordEncoder.matches(rawToken, refreshTokenEntity.getToken())) {
            throw new RefreshTokenException();
        }

        if (refreshTokenEntity.getExpiryDate().isBefore(Instant.now())) {
            throw new ExpiredRefreshTokenException();
        }

//        TODO: Check if user is disabled (also in the JWT generation)
        User userEntity = userRepository.findByEmail(refreshTokenEntity.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not available"));

        String newRefreshToken = refreshExistingToken(refreshTokenEntity);
        String newJwt = generateJwtByUsername(userEntity.getEmail());
        return new NewTokenAuthenticationDTO(newRefreshToken, newJwt);
    }

    private String generateJwtByUsername(String username) {
        var now = Instant.now();
        var expiresAt = now.plusSeconds(jwtExpiryMinutes * 60);

        User userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not available"));

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

    private String generateNewRefreshToken(String username) {
        var now = Instant.now();
        var expiry = now.plusSeconds( refreshExpiryHours * 60 * 60);

        String encodedSecret = getRandomBase64Secret();
        String encryptedSecret = passwordEncoder.encode(encodedSecret);
        String publicKey = Base64.encode(UUID.randomUUID().toString()).toString();
        UUID familyId = UUID.randomUUID();
        RefreshToken newToken = new RefreshToken(publicKey, encryptedSecret, username, expiry, familyId);

        refreshTokenRepository.save(newToken);
        return publicKey + "." + encodedSecret;
    }

    private String refreshExistingToken(RefreshToken oldToken) {
        String encodedSecret = getRandomBase64Secret();
        String encryptedSecret = passwordEncoder.encode(encodedSecret);
        String publicKey = Base64.encode(UUID.randomUUID().toString()).toString();
        String username = oldToken.getUsername();
        Instant expiryDate = oldToken.getExpiryDate();
        UUID familyId = oldToken.getFamilyId();

        RefreshToken newToken = new RefreshToken(publicKey, encryptedSecret, username, expiryDate, familyId);
        refreshTokenRepository.save(newToken);
        refreshTokenRepository.revokeByPublicKey(oldToken.getLookup());
        return publicKey + "." + encodedSecret;
    }

    private String getRandomBase64Secret() {
        SecureRandom random = new SecureRandom();
        byte[] secret = new byte[32];
        random.nextBytes(secret);
        return Base64.encode(secret).toString();
    }

    private void invalidateTokenFamily(UUID familyId) {
        refreshTokenRepository.revokeByFamilyId(familyId);
    }

}
