package com.agroconecta.agroconecta.service;

import com.agroconecta.agroconecta.auth.JwtService;
import com.agroconecta.agroconecta.model.TokenInvalido;
import com.agroconecta.agroconecta.repository.TokenInvalidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenInvalidoService {
    private final TokenInvalidoRepository tokenInvalidoRepository;
    private final JwtService jwtService;

    @Transactional
    public void invalidarToken(String token) {
        if (!tokenInvalidoRepository.existsByToken(token)) {
            TokenInvalido tokenInvalido = TokenInvalido.builder()
                    .token(token)
                    .fechaExpiracion(
                            jwtService.getExpirationDateFromToken(token)
                                    .toInstant()
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDateTime()
                    )
                    .build();

            tokenInvalidoRepository.save(tokenInvalido);
        }
    }

    public boolean isTokenInvalido(String token) {
        return tokenInvalidoRepository.existsByToken(token);
    }

    //Se ejecuta automáticamente cada día a las 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void limpiarTokensExpirados() {
        tokenInvalidoRepository.deleteByFechaExpiracionBefore(LocalDateTime.now());
    }
}
