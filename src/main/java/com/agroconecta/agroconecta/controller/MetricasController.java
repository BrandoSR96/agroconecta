package com.agroconecta.agroconecta.controller;

import com.agroconecta.agroconecta.dto.ListarProductosAgricultorResponse;
import com.agroconecta.agroconecta.dto.MetricasResponse;
import com.agroconecta.agroconecta.service.MetricasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agricultor")
@RequiredArgsConstructor
public class MetricasController {
    private final MetricasService metricasService;

    @GetMapping("/{id}/metricas")
    public ResponseEntity<MetricasResponse> obtenerMetricas(@PathVariable String id) {
        MetricasResponse response = metricasService.obtenerMetricasAgricultor(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/productos")
    public ResponseEntity<ListarProductosAgricultorResponse> listarProductos(
            @PathVariable String id,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ListarProductosAgricultorResponse response = metricasService.listarProductosAgricultor(
                id, estado, page, size);
        return ResponseEntity.ok(response);
    }
}
