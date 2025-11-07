package com.agroconecta.agroconecta.service;

import com.agroconecta.agroconecta.dto.*;
import com.agroconecta.agroconecta.exception.ProductoNotFoundException;
import com.agroconecta.agroconecta.exception.UnauthorizedAccessException;
import com.agroconecta.agroconecta.mapper.ProductoMapper;
import com.agroconecta.agroconecta.model.Producto;
import com.agroconecta.agroconecta.model.Usuario;
import com.agroconecta.agroconecta.repository.ProductoRepository;
import com.agroconecta.agroconecta.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoMapper productoMapper;

    @Transactional
    public ProductoResponse registrarProducto(ProductoRequest requestDTO, Authentication authentication) {
        String emailUsuario = authentication.getName();
        Usuario agricultor = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        try {
            UUID requestAgricultorId = UUID.fromString(requestDTO.getAgricultorId());
            if (!agricultor.getId().equals(requestAgricultorId)) {
                throw new UnauthorizedAccessException("No puedes crear productos para otro usuario");
            }
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedAccessException("ID de agricultor inválido");
        }

        Producto producto = productoMapper.toEntity(requestDTO, agricultor);
        Producto productoGuardado = productoRepository.save(producto);

        return productoMapper.toResponseDTO(productoGuardado);
    }

    @Transactional(readOnly = true)
    public ListaProductosResponse listarProductosUsuario(Authentication authentication, int page, int size) {
        String emailUsuario = authentication.getName();
        Usuario agricultor = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productosPage = productoRepository.findByAgricultor(agricultor, pageable);
        List<ProductoResumen> productos = productosPage.getContent().stream()
                .map(productoMapper::toResumenDTO)
                .collect(Collectors.toList());
        return ListaProductosResponse.builder()
                .page(page)
                .size(size)
                .totalItems(productosPage.getTotalElements())
                .totalPages(productosPage.getTotalPages())
                .productos(productos)
                .build();
    }

    @Transactional(readOnly = true)
    public ProductoDetalle obtenerDetalleProducto(String id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
        return productoMapper.toDetalleDTO(producto);
    }

    @Transactional
    public ProductoEliminadoResponse eliminarProducto(String id, Authentication authentication) {
        String emailUsuario = authentication.getName();
        Usuario agricultor = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));

        if (!producto.getAgricultor().getId().equals(agricultor.getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para eliminar este producto");
        }

        productoRepository.delete(producto);
        return ProductoEliminadoResponse.builder()
                .mensaje("Producto eliminado correctamente.")
                .id(id)
                .build();
    }

    @Transactional
    public ProductoUpdateResponse actualizarProducto(String id, ProductoUpdateRequest updateDTO, Authentication authentication) {
        String emailUsuario = authentication.getName();
        Usuario agricultor = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));

        if (!producto.getAgricultor().getId().equals(agricultor.getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para actualizar este producto");
        }

        productoMapper.updateEntityFromDTO(producto, updateDTO);
        Producto productoActualizado = productoRepository.save(producto);
        return productoMapper.toUpdateResponseDTO(productoActualizado);
    }
}
