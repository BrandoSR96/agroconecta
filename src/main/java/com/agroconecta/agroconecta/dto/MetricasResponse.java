package com.agroconecta.agroconecta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricasResponse {
    private String agricultorId;
    private Integer totalProductos;
    private Integer productosActivos;
    private Integer productosInactivos;
    private Integer vistasTotales;
    private Integer contactosRecibidos;
    private LocalDateTime fechaUltimaActividad;
}
