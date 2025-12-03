package com.agroconecta.agroconecta.repository;

import com.agroconecta.agroconecta.model.Producto;
import com.agroconecta.agroconecta.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    Page<Producto> findByAgricultor(Usuario agricultor, Pageable pageable);
    boolean existsByIdAndAgricultor(String id, Usuario agricultor);

    // búsqueda por palabra clave
    @Query("SELECT p FROM Producto p WHERE p.estado = 'ACTIVO' AND " +
            "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.categoria) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Producto> buscarPorPalabraClave(@Param("query") String query, Pageable pageable);

    // filtro avanzado
    @Query("SELECT p FROM Producto p WHERE p.estado = 'ACTIVO' " +
            "AND (:categoria IS NULL OR LOWER(p.categoria) = LOWER(:categoria)) " +
            "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax) " +
            "ORDER BY " +
            "CASE WHEN :orden = 'ASC' THEN p.precio END ASC, " +
            "CASE WHEN :orden = 'DESC' THEN p.precio END DESC, " +
            "CASE WHEN :orden IS NULL THEN p.fechaRegistro END DESC")
    Page<Producto> filtrarProductos(
            @Param("categoria") String categoria,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            @Param("orden") String orden,
            Pageable pageable);
}
