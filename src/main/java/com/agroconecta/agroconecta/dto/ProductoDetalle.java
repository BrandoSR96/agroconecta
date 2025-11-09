package com.agroconecta.agroconecta.dto;

import com.agroconecta.agroconecta.enums.EstadoProducto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDetalle {
    private String id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer cantidad;
    private String imagenUrl;
    private String categoria;
    private String agricultorId;
    private EstadoProducto estado;
    private LocalDateTime fechaRegistro;
}
