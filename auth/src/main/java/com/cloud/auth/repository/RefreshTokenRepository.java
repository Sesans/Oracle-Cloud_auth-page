package com.cloud.auth.repository;

import com.cloud.auth.domain.RefreshToken;
import com.cloud.auth.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Transactional
    @Modifying
    void deleteByUser(User user);

    Optional<RefreshToken> findByToken(String refreshToken);
}
