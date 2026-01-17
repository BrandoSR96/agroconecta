package com.agroconecta.agroconecta.service;

import com.agroconecta.agroconecta.dto.ListarProductosAgricultorResponse;
import com.agroconecta.agroconecta.dto.MetricasResponse;
import com.agroconecta.agroconecta.dto.ProductoResumen;
import com.agroconecta.agroconecta.enums.EstadoProducto;
import com.agroconecta.agroconecta.exception.ResourceNotFoundException;
import com.agroconecta.agroconecta.mapper.ProductoMapper;
import com.agroconecta.agroconecta.model.Producto;
import com.agroconecta.agroconecta.model.Usuario;
import com.agroconecta.agroconecta.repository.ProductoRepository;
import com.agroconecta.agroconecta.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricasService {
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoMapper productoMapper;

    @Transactional(readOnly = true)
    public MetricasResponse obtenerMetricasAgricultor(String agricultorId){
        UUID uuid;
        try {
            uuid = UUID.fromString(agricultorId);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("ID de agricultor inválido");
        }

        Usuario agricultor = usuarioRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Agricultor no encontrado con ID: " + agricultorId));

        Integer totalProductos = productoRepository.contarProductosPorAgricultor(uuid);
        Integer productosActivos = productoRepository.contarProductosPorAgricultorYEstado(
                uuid, EstadoProducto.ACTIVO);
        Integer productosInactivos = productoRepository.contarProductosPorAgricultorYEstado(
                uuid, EstadoProducto.INACTIVO);

        // Simulación de métricas adicionales
        Integer vistasTotales = totalProductos * 28; // Simulado
        Integer contactosRecibidos = productosActivos * 2; // Simulado

        LocalDateTime ultimaActividad = productoRepository.findUltimaActividadPorAgricultor(uuid);
        if (ultimaActividad == null) {
            ultimaActividad = agricultor.getFechaRegistro();
        }

        return MetricasResponse.builder()
                .agricultorId(agricultorId)
                .totalProductos(totalProductos)
                .productosActivos(productosActivos)
                .productosInactivos(productosInactivos)
                .vistasTotales(vistasTotales)
                .contactosRecibidos(contactosRecibidos)
                .fechaUltimaActividad(ultimaActividad)
                .build();
    }

    @Transactional(readOnly = true)
    public ListarProductosAgricultorResponse listarProductosAgricultor(
            String agricultorId,
            String estado,
            int page,
            int size) {

        UUID uuid;
        try {
            uuid = UUID.fromString(agricultorId);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("ID de agricultor inválido");
        }

        Usuario agricultor = usuarioRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Agricultor no encontrado con ID: " + agricultorId));

        EstadoProducto estadoProducto = null;
        if (estado != null && !estado.isBlank()) {
            try {
                estadoProducto = EstadoProducto.valueOf(estado.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado inválido: " + estado);
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productosPage = productoRepository.findByAgricultorIdAndEstado(
                uuid, estadoProducto, pageable);

        List<ProductoResumen> productos = productosPage.getContent().stream()
                .map(productoMapper::toResumenDTO)
                .collect(Collectors.toList());

        return ListarProductosAgricultorResponse.builder()
                .agricultorId(agricultorId)
                .page(page)
                .size(size)
                .totalItems(productosPage.getTotalElements())
                .totalPages(productosPage.getTotalPages())
                .productos(productos)
                .build();
    }
}
