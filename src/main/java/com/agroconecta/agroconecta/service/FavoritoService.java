package com.agroconecta.agroconecta.service;

import com.agroconecta.agroconecta.dto.FavoritoResponse;
import com.agroconecta.agroconecta.exception.FavoritoNotFoundException;
import com.agroconecta.agroconecta.exception.FavoritoYaExisteException;
import com.agroconecta.agroconecta.exception.ProductoNotFoundException;
import com.agroconecta.agroconecta.model.Favorito;
import com.agroconecta.agroconecta.model.Producto;
import com.agroconecta.agroconecta.model.Usuario;
import com.agroconecta.agroconecta.repository.FavoritoRepository;
import com.agroconecta.agroconecta.repository.ProductoRepository;
import com.agroconecta.agroconecta.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoritoService {
    private final FavoritoRepository favoritoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public FavoritoResponse agregarFavorito(String productoId, Authentication authentication) {
        String emailUsuario = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + productoId));

        // Verificar si ya existe en favoritos
        if (favoritoRepository.existsByUsuarioAndProducto(usuario, producto)) {
            throw new FavoritoYaExisteException("El producto ya está en tus favoritos");
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setProducto(producto);

        Favorito favoritoGuardado = favoritoRepository.save(favorito);

        return FavoritoResponse.builder()
                .mensaje("Producto agregado a favoritos exitosamente")
                .productoId(producto.getId())
                .usuarioId(usuario.getId().toString())
                .fechaAgregado(favoritoGuardado.getFechaAgregado())
                .build();
    }

    @Transactional
    public void eliminarFavorito(String productoId, Authentication authentication) {
        String emailUsuario = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + productoId));

        Favorito favorito = favoritoRepository.findByUsuarioAndProducto(usuario, producto)
                .orElseThrow(() -> new FavoritoNotFoundException("El producto no está en tus favoritos"));

        favoritoRepository.delete(favorito);
    }
}
