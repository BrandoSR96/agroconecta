package com.agroconecta.agroconecta.dto;

import com.agroconecta.agroconecta.enums.MetodoPago;
import com.agroconecta.agroconecta.enums.TipoPlan;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoRequest {
    @NotBlank(message = "El ID del agricultor es obligatorio")
    private String agricultorId;

    @NotNull(message = "El plan es obligatorio")
    private TipoPlan plan;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    private String referencia;
}
