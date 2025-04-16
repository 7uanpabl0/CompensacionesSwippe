package com.example.compensaciones.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bancolombia_transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BancoTransaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;

    private String monedaOrigen;
    private String monedaDestino;
    private BigDecimal tipoCambio;
    private boolean ejecutado;
    private LocalDateTime fechaRegistro;
    private UUID paquete;
}