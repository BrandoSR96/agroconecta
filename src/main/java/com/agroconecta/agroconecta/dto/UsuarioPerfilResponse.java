package com.agroconecta.agroconecta.dto;

import com.agroconecta.agroconecta.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPerfilResponse {
    private UUID id;
    private String nombre;
    private String apellido;
    private String email;
    private Rol rol;
    private LocalDateTime fechaRegistro;
    private Boolean activo;
}
