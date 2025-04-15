package com.example.compensaciones.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compensaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compensacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;

    private String monedaOrigen;

    private String monedaDestino;

    private BigDecimal tipoCambio;

    private BigDecimal montoConvertido;

    private boolean ejecutado;
}
