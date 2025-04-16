package com.example.compensaciones.domain.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bancolombia_paquetes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BancoPaquete {

    @Id
    private UUID id;

    private LocalDateTime fechaRecepcion;

    @Enumerated(EnumType.STRING)
    private EstadoPaquete estado;

    @Column(name = "monto_total", nullable = false)
    private BigDecimal montoTotal;


    @OneToMany(mappedBy = "paquete", cascade = CascadeType.ALL)
    private List<BancoTransaccion> transacciones;
}
