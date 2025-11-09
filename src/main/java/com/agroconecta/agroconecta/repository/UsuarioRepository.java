package com.agroconecta.agroconecta.repository;

import com.agroconecta.agroconecta.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    @Override
    Optional<Usuario> findById(UUID uuid);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
