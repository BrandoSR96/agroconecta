package com.agroconecta.agroconecta.service;

import com.agroconecta.agroconecta.dto.PagoRequest;
import com.agroconecta.agroconecta.dto.PagoResponse;
import com.agroconecta.agroconecta.exception.ResourceNotFoundException;
import com.agroconecta.agroconecta.model.Usuario;
import com.agroconecta.agroconecta.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonetizacionService {
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PagoResponse registrarPago(PagoRequest request) {
        // Validar que el agricultor existe
        UUID agricultorId;
        try {
            agricultorId = UUID.fromString(request.getAgricultorId());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("ID de agricultor inválido");
        }

        Usuario agricultor = usuarioRepository.findById(agricultorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Agricultor no encontrado con ID: " + request.getAgricultorId()));

        // Generar ID unico para el pago
        String idPago = "pago-" + UUID.randomUUID().toString().substring(0, 12);

        // Log simulado
        log.info("Pago simulado - Agricultor: {}, Plan: {}, Monto: {}, Método: {}",
                agricultor.getNombre(),
                request.getPlan(),
                request.getMonto(),
                request.getMetodoPago());

        // En producción, (aquí se guardaría en una tabla de pagos/suscripciones)
        return PagoResponse.builder()
                .idPago(idPago)
                .agricultorId(request.getAgricultorId())
                .plan(request.getPlan())
                .monto(request.getMonto())
                .metodoPago(request.getMetodoPago())
                .referencia(request.getReferencia())
                .estado("SIMULADO")
                .fechaPago(LocalDateTime.now())
                .mensaje("Pago registrado exitosamente. El plan " +
                        request.getPlan() + " está activo.")
                .build();
    }
}
