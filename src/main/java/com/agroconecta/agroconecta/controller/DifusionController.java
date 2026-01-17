package com.agroconecta.agroconecta.controller;

import com.agroconecta.agroconecta.dto.DifusionRequest;
import com.agroconecta.agroconecta.dto.DifusionResponse;
import com.agroconecta.agroconecta.service.DifusionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/difusion")
@RequiredArgsConstructor
public class DifusionController {
    private final DifusionService difusionService;

    @PostMapping("/enviar")
    public ResponseEntity<DifusionResponse> enviarDifusion(
            @Valid @RequestBody DifusionRequest request) {
        DifusionResponse response = difusionService.enviarDifusion(request);
        return ResponseEntity.ok(response);
    }
}
