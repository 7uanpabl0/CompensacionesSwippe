package com.example.compensaciones.client.dto;

import com.example.compensaciones.application.dto.CompensacionResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CompensacionPaqueteDto {
    private UUID paqueteId;
    private String paisDestino;
    private int cantidadTransacciones;
    private List<CompensacionResponseDto> transacciones;
}