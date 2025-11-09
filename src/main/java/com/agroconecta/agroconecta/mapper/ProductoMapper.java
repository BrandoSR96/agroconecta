package com.agroconecta.agroconecta.mapper;

import com.agroconecta.agroconecta.dto.*;
import com.agroconecta.agroconecta.model.Producto;
import com.agroconecta.agroconecta.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public Producto toEntity(ProductoRequest dto, Usuario agricultor) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(dto.getCategoria());
        producto.setAgricultor(agricultor);
        return producto;
    }

    public ProductoResponse toResponseDTO(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .cantidad(producto.getCantidad())
                .imagenUrl(producto.getImagenUrl())
                .categoria(producto.getCategoria())
                .agricultorId(producto.getAgricultor().getId().toString())
                .estado(producto.getEstado())
                .fechaRegistro(producto.getFechaRegistro())
                .build();
    }

    public ProductoResumen toResumenDTO(Producto producto) {
        return ProductoResumen.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .cantidad(producto.getCantidad())
                .imagenUrl(producto.getImagenUrl())
                .categoria(producto.getCategoria())
                .estado(producto.getEstado())
                .fechaRegistro(producto.getFechaRegistro())
                .build();
    }

    public ProductoDetalle toDetalleDTO(Producto producto) {
        return ProductoDetalle.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .cantidad(producto.getCantidad())
                .imagenUrl(producto.getImagenUrl())
                .categoria(producto.getCategoria())
                .agricultorId(producto.getAgricultor().getId().toString())
                .estado(producto.getEstado())
                .fechaRegistro(producto.getFechaRegistro())
                .build();
    }

    public ProductoUpdateResponse toUpdateResponseDTO(Producto producto) {
        return ProductoUpdateResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .cantidad(producto.getCantidad())
                .imagenUrl(producto.getImagenUrl())
                .categoria(producto.getCategoria())
                .agricultorId(producto.getAgricultor().getId().toString())
                .estado(producto.getEstado())
                .fechaActualizacion(producto.getFechaActualizacion())
                .build();
    }

    public void updateEntityFromDTO(Producto producto, ProductoUpdateRequest dto) {
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(dto.getCategoria());
    }
}
