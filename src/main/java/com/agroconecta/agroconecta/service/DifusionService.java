package com.agroconecta.agroconecta.service;

import com.agroconecta.agroconecta.dto.DetalleEnvio;
import com.agroconecta.agroconecta.dto.DifusionRequest;
import com.agroconecta.agroconecta.dto.DifusionResponse;
import com.agroconecta.agroconecta.enums.CanalDifusion;
import com.agroconecta.agroconecta.exception.ProductoNotFoundException;
import com.agroconecta.agroconecta.model.Producto;
import com.agroconecta.agroconecta.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class DifusionService {
    private final ProductoRepository productoRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{9,15}$");

    @Transactional(readOnly = true)
    public DifusionResponse enviarDifusion(DifusionRequest request) {
        // Validar que el producto existe
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ProductoNotFoundException(
                        "Producto no encontrado con ID: " + request.getProductoId()));

        // Validar destinatarios según el canal
        validarDestinatarios(request.getCanal(), request.getDestinatarios());

        // Simular el envío
        List<DetalleEnvio> detalles = new ArrayList<>();
        for (String destinatario : request.getDestinatarios()) {
            DetalleEnvio detalle = DetalleEnvio.builder()
                    .destinatario(destinatario)
                    .resultado("Enviado")
                    .build();
            detalles.add(detalle);

            // Log simulado
            log.info("Difusión simulada - Canal: {}, Destinatario: {}, Producto: {}",
                    request.getCanal(), destinatario, producto.getNombre());
        }

        return DifusionResponse.builder()
                .productoId(request.getProductoId())
                .canal(request.getCanal())
                .mensaje(request.getMensaje())
                .totalEnviados(request.getDestinatarios().size())
                .estado("SIMULADO")
                .fechaEnvio(LocalDateTime.now())
                .detalle(detalles)
                .build();
    }

    private void validarDestinatarios(CanalDifusion canal, List<String> destinatarios) {
        for (String destinatario : destinatarios) {
            switch (canal) {
                case EMAIL:
                    if (!EMAIL_PATTERN.matcher(destinatario).matches()) {
                        throw new IllegalArgumentException(
                                "Email inválido: " + destinatario);
                    }
                    break;
                case WHATSAPP:
                case SMS:
                    if (!PHONE_PATTERN.matcher(destinatario).matches()) {
                        throw new IllegalArgumentException(
                                "Número de teléfono inválido: " + destinatario);
                    }
                    break;
            }
        }
    }
}
