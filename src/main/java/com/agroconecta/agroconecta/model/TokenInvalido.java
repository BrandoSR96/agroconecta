package com.agroconecta.agroconecta.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_invalidados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenInvalido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false, unique = true)
    private String token;

    @Column(name = "fecha_invalidacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaInvalidacion = LocalDateTime.now();

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;
}
