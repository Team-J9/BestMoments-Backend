package com.j9.bestmoments.repository;

import com.j9.bestmoments.domain.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByAccessToken(String accessToken);
    Optional<Token> findByRefreshToken(String refreshToken);

}
