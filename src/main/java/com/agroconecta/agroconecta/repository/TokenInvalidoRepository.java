package com.agroconecta.agroconecta.repository;

import com.agroconecta.agroconecta.model.TokenInvalido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TokenInvalidoRepository extends JpaRepository<TokenInvalido, Long> {
    boolean existsByToken(String token);
    void deleteByFechaExpiracionBefore(LocalDateTime fecha);
}
