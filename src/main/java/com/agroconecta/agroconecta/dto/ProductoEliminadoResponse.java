package com.agroconecta.agroconecta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoEliminadoResponse {
    private String mensaje;
    private String id;
}
