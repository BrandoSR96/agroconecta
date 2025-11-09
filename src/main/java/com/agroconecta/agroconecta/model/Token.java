package com.agroconecta.agroconecta.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    private boolean revoked;
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
