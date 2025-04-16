package com.example.compensaciones.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banco_destino")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BancoDestino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String moneda;
    private String pais;
    private String endpoint;
}
