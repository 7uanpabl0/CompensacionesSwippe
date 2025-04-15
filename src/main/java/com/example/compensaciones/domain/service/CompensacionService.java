package com.example.compensaciones.domain.service;

import com.example.compensaciones.application.dto.CompensacionRequestDto;
import com.example.compensaciones.application.dto.CompensacionResponseDto;

import java.util.Optional;

public interface CompensacionService {
    CompensacionResponseDto registrarCompensacion(CompensacionRequestDto requestDto);
    Optional<CompensacionResponseDto> obtenerCompensacionPorId(Long id);
    CompensacionResponseDto ejecutarCompensacion();
}
