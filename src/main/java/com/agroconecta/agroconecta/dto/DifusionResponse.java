package com.agroconecta.agroconecta.dto;

import com.agroconecta.agroconecta.enums.CanalDifusion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DifusionResponse {
    private String productoId;
    private CanalDifusion canal;
    private String mensaje;
    private Integer totalEnviados;
    private String estado;
    private LocalDateTime fechaEnvio;
    private List<DetalleEnvio> detalle;
}
