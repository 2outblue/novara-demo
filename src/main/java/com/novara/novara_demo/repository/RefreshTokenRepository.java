package com.novara.novara_demo.repository;

import com.novara.novara_demo.model.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUsername(String username);
    Optional<RefreshToken> findByLookup(String lookup);

    @Modifying
    @Query("update RefreshToken t set t.revoked = true where t.familyId = :familyId and t.revoked = false")
    @Transactional
    void revokeByFamilyId(@Param("familyId") UUID familyId);

    @Modifying
    @Query("update RefreshToken t set t.revoked = true where t.lookup = :publicKey")
    @Transactional
    void revokeByPublicKey(@Param("publicKey") String publicKey);

}
