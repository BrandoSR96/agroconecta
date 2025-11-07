package com.agroconecta.agroconecta.repository;

import com.agroconecta.agroconecta.model.Producto;
import com.agroconecta.agroconecta.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    Page<Producto> findByAgricultor(Usuario agricultor, Pageable pageable);
    boolean existsByIdAndAgricultor(String id, Usuario agricultor);
}
