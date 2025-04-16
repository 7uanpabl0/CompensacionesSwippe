package com.example.compensaciones.controller;

import com.example.compensaciones.application.dto.CompensacionRequestDto;
import com.example.compensaciones.application.dto.CompensacionResponseDto;
import com.example.compensaciones.domain.service.CompensacionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compensaciones")
@RequiredArgsConstructor
public class CompensacionController {

    private final CompensacionService compensacionService;

    @PostMapping
    public ResponseEntity<CompensacionResponseDto> registrar(@Valid @RequestBody CompensacionRequestDto requestDto) {
        CompensacionResponseDto response = compensacionService.registrarCompensacion(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompensacionResponseDto> obtenerPorId(
            @PathVariable @Positive(message = "El ID debe ser mayor que 0") Long id) {
        CompensacionResponseDto dto = compensacionService.obtenerCompensacionPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/ejecutar")
    public ResponseEntity<String> ejecutar() {
        compensacionService.ejecutarCompensacion();
        return ResponseEntity.ok("Paquetes generados y enviados por país");
    }
}
