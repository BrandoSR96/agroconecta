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
public class FavoritoResponse {
    private String mensaje;
    private String productoId;
    private String usuarioId;
    private LocalDateTime fechaAgregado;
}
