package com.agroconecta.agroconecta.controller;

import com.agroconecta.agroconecta.dto.FavoritoResponse;
import com.agroconecta.agroconecta.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favoritos")
@RequiredArgsConstructor
public class FavoritoController {
    private final FavoritoService favoritoService;

    @PostMapping("/{id}")
    public ResponseEntity<FavoritoResponse> agregarFavorito(
            @PathVariable String id,
            Authentication authentication) {
        FavoritoResponse response = favoritoService.agregarFavorito(id, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFavorito(
            @PathVariable String id,
            Authentication authentication) {
        favoritoService.eliminarFavorito(id, authentication);
        return ResponseEntity.noContent().build();
    }
}