package com.agroconecta.agroconecta.controller;

import com.agroconecta.agroconecta.dto.PagoRequest;
import com.agroconecta.agroconecta.dto.PagoResponse;
import com.agroconecta.agroconecta.service.MonetizacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/monetizacion")
@RequiredArgsConstructor
public class MonetizacionController {
    private final MonetizacionService monetizacionService;

    @PostMapping("/pago")
    public ResponseEntity<PagoResponse> registrarPago(
            @Valid @RequestBody PagoRequest request) {
        PagoResponse response = monetizacionService.registrarPago(request);
        return ResponseEntity.ok(response);
    }
}
