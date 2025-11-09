package com.agroconecta.agroconecta.mapper;

import com.agroconecta.agroconecta.dto.UsuarioPerfilResponse;
import com.agroconecta.agroconecta.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public UsuarioPerfilResponse toPerfilResponse(Usuario usuario){
        return UsuarioPerfilResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .fechaRegistro(usuario.getFechaRegistro())
                .activo(usuario.getActivo())
                .build();
    }
}
