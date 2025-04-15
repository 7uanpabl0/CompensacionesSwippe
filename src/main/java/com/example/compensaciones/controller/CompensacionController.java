package com.example.compensaciones.controller;

import com.example.compensaciones.application.dto.CompensacionRequestDto;
import com.example.compensaciones.application.dto.CompensacionResponseDto;
import com.example.compensaciones.domain.service.CompensacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compensaciones")
@RequiredArgsConstructor
public class CompensacionController {

    private final CompensacionService compensacionService;

    @PostMapping
    public ResponseEntity<CompensacionResponseDto> registrar(@RequestBody CompensacionRequestDto requestDto) {
        CompensacionResponseDto response = compensacionService.registrarCompensacion(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompensacionResponseDto> obtenerPorId(@PathVariable Long id) {
        return compensacionService.obtenerCompensacionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/ejecutar")
    public ResponseEntity<CompensacionResponseDto> ejecutar() {
        CompensacionResponseDto response = compensacionService.ejecutarCompensacion();
        return ResponseEntity.ok(response);
    }
}
