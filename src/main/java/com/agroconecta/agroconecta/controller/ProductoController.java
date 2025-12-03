package com.agroconecta.agroconecta.controller;

import com.agroconecta.agroconecta.dto.*;
import com.agroconecta.agroconecta.service.ProductoService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponse> registrarProducto(
            @Valid @RequestBody ProductoRequest requestDTO,
            Authentication authentication) {
        ProductoResponse response = productoService.registrarProducto(requestDTO, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ListaProductosResponse> listarProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        ListaProductosResponse response = productoService.listarProductosUsuario(authentication, page, size);
        return ResponseEntity.ok(response);
    }

    @PermitAll
    @GetMapping("/detalle/{id}")
    public ResponseEntity<ProductoDetalle> obtenerDetalleProducto(@PathVariable String id) {
        ProductoDetalle response = productoService.obtenerDetalleProducto(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ProductoEliminadoResponse> eliminarProducto(
            @PathVariable String id,
            Authentication authentication) {
        ProductoEliminadoResponse response = productoService.eliminarProducto(id, authentication);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoUpdateResponse> actualizarProducto(
            @PathVariable String id,
            @Valid @RequestBody ProductoUpdateRequest updateDTO,
            Authentication authentication) {
        ProductoUpdateResponse response = productoService.actualizarProducto(id, updateDTO, authentication);
        return ResponseEntity.ok(response);
    }

    @PermitAll
    @GetMapping("/buscar")
    public ResponseEntity<ListaProductosResponse> buscarProductos(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ListaProductosResponse response = productoService.buscarProductos(query, page, size);
        return ResponseEntity.ok(response);
    }

    @PermitAll
    @GetMapping("/filtro")
    public ResponseEntity<ListaProductosResponse> filtrarProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) String orden,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ListaProductosResponse response = productoService.filtrarProductos(
                categoria, precioMin, precioMax, orden, page, size);
        return ResponseEntity.ok(response);
    }

}
