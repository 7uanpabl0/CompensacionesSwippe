package com.example.compensaciones.domain.service;

import com.example.compensaciones.application.dto.CompensacionRequestDto;
import com.example.compensaciones.application.dto.CompensacionResponseDto;

import java.util.Optional;

public interface CompensacionService {
    CompensacionResponseDto registrarCompensacion(CompensacionRequestDto requestDto);
    CompensacionResponseDto  obtenerCompensacionPorId(Long id);
    public void ejecutarCompensacion();
}
