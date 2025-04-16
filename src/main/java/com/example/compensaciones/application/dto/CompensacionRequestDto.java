package com.example.compensaciones.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompensacionRequestDto {
    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "La moneda de origen es obligatoria")
    @Size(min = 3, max = 3, message = "La moneda de origen debe tener 3 letras")
    private String monedaOrigen;

    @NotBlank(message = "La moneda de destino es obligatoria")
    @Size(min = 3, max = 3, message = "La moneda de destino debe tener 3 letras")
    private String monedaDestino;
}
