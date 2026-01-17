package com.agroconecta.agroconecta.dto;

import com.agroconecta.agroconecta.enums.CanalDifusion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DifusionRequest {
    @NotBlank(message = "El ID del producto es obligatorio")
    private String productoId;

    @NotNull(message = "El canal es obligatorio")
    private CanalDifusion canal;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotEmpty(message = "Debe haber al menos un destinatario")
    private List<String> destinatarios;
}
