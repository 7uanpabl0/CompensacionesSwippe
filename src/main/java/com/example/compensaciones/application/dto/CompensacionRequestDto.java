package com.example.compensaciones.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompensacionRequestDto {
    private BigDecimal monto;
    private String monedaOrigen;
    private String monedaDestino;
}
