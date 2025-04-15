package com.example.compensaciones.error;

import com.example.compensaciones.client.dto.ApiErrorDto;
import com.example.compensaciones.domain.exception.TipoCambioNoDisponibleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TipoCambioNoDisponibleException.class)
    public ResponseEntity<ApiErrorDto> handleTipoCambioNoDisponible(TipoCambioNoDisponibleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiErrorDto.builder()
                        .mensaje("Error al obtener tipo de cambio")
                        .detalle(ex.getMessage())
                        .codigo(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleValidacion(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiErrorDto.builder()
                        .mensaje("Error de validación")
                        .detalle(ex.getBindingResult().getFieldError().getDefaultMessage())
                        .codigo(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiErrorDto.builder()
                        .mensaje("Error inesperado")
                        .detalle(ex.getMessage())
                        .codigo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}