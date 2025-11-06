package com.agroconecta.agroconecta.service;

import com.agroconecta.agroconecta.dto.UsuarioPerfilResponse;
import com.agroconecta.agroconecta.exception.ResourceNotFoundException;
import com.agroconecta.agroconecta.mapper.UsuarioMapper;
import com.agroconecta.agroconecta.model.Usuario;
import com.agroconecta.agroconecta.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public UsuarioPerfilResponse obtenerPerfilUsuarioAutenticado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con email: " + email));

        return usuarioMapper.toPerfilResponse(usuario);
    }
}
