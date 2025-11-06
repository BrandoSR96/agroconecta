package com.agroconecta.agroconecta.mapper;

import com.agroconecta.agroconecta.dto.LoginResponse;
import com.agroconecta.agroconecta.dto.RegisterResponse;
import com.agroconecta.agroconecta.dto.UsuarioDTO;
import com.agroconecta.agroconecta.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public static RegisterResponse toRegisterResponse(Usuario usuario) {
        return RegisterResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .fechaRegistro(usuario.getFechaRegistro())
                .mensaje("Registro exitoso. Bienvenido a AgroConecta.")
                .build();
    }

    public static LoginResponse toLoginResponse(Usuario usuario, String token) {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();

        return LoginResponse.builder()
                .token(token)
                .usuario(usuarioDTO)
                .mensaje("Inicio de sesión exitoso.")
                .build();
    }
}
