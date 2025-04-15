package com.example.compensaciones.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompensacionResponseDto {
    private Long id;
    private BigDecimal monto;
    private String monedaOrigen;
    private String monedaDestino;
    private BigDecimal tipoCambio;
    private BigDecimal montoConvertido;
    private LocalDateTime fechaRegistro;
    private boolean ejecutado;
}
