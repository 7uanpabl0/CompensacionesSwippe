package com.example.compensaciones.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize
public class CompensacionResponseDto implements Serializable {
    private Long id;
    private BigDecimal monto;
    private String monedaOrigen;
    private String monedaDestino;
    private BigDecimal tipoCambio;
    private BigDecimal montoConvertido;
    private boolean ejecutado;
}
