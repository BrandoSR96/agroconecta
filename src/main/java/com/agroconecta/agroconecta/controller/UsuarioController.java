package com.agroconecta.agroconecta.controller;

import com.agroconecta.agroconecta.dto.UsuarioPerfilResponse;
import com.agroconecta.agroconecta.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioPerfilResponse> obtenerPerfil() {
        UsuarioPerfilResponse perfil = usuarioService.obtenerPerfilUsuarioAutenticado();
        return ResponseEntity.ok(perfil);
    }
}
