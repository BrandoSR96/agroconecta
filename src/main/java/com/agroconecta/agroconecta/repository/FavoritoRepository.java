package com.agroconecta.agroconecta.repository;

import com.agroconecta.agroconecta.model.Favorito;
import com.agroconecta.agroconecta.model.Producto;
import com.agroconecta.agroconecta.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, String> {
    Optional<Favorito> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    boolean existsByUsuarioAndProducto(Usuario usuario, Producto producto);
}
