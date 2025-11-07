package com.agroconecta.agroconecta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaProductosResponse {
    private Integer page;
    private Integer size;
    private Long totalItems;
    private Integer totalPages;
    private List<ProductoResumen> productos;
}
