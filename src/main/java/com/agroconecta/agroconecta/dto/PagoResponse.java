package com.agroconecta.agroconecta.dto;

import com.agroconecta.agroconecta.enums.MetodoPago;
import com.agroconecta.agroconecta.enums.TipoPlan;
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
public class PagoResponse {
    private String idPago;
    private String agricultorId;
    private TipoPlan plan;
    private BigDecimal monto;
    private MetodoPago metodoPago;
    private String referencia;
    private String estado;
    private LocalDateTime fechaPago;
    private String mensaje;
}
