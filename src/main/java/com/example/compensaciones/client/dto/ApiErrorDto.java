package com.example.compensaciones.client.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiErrorDto {
    private String mensaje;
    private String detalle;
    private int codigo;
    private LocalDateTime timestamp;
}
