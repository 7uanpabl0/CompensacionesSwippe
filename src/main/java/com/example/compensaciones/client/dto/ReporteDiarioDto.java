package com.example.compensaciones.client.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReporteDiarioDto {
    private String moneda;
    private int totalTransacciones;
    private int ejecutadas;
    private int noEjecutadas;
    private BigDecimal montoTotal;
}
