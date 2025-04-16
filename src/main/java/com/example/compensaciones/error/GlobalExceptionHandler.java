package com.example.compensaciones.error;

import com.example.compensaciones.client.dto.ApiErrorDto;
import com.example.compensaciones.domain.exception.BancoDestinoNoEncontradoException;
import com.example.compensaciones.domain.exception.CompensacionNoEncontradaException;
import com.example.compensaciones.domain.exception.TipoCambioNoDisponibleException;
import com.example.compensaciones.domain.exception.TransaccionDuplicadaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

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

    @ExceptionHandler(TransaccionDuplicadaException.class)
    public ResponseEntity<ApiErrorDto> handleDuplicada(TransaccionDuplicadaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiErrorDto.builder()
                        .mensaje("Transacción duplicada")
                        .detalle(ex.getMessage())
                        .codigo(HttpStatus.CONFLICT.value())
                        .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                        .build()
        );
    }

    @ExceptionHandler(BancoDestinoNoEncontradoException.class)
    public ResponseEntity<ApiErrorDto> handleBancoNoEncontrado(BancoDestinoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiErrorDto.builder()
                        .mensaje("Banco no encontrado")
                        .detalle(ex.getMessage())
                        .codigo(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        return ResponseEntity.badRequest().body(
                ApiErrorDto.builder()
                        .mensaje("Error de validación de campos")
                        .detalle(mensaje)
                        .codigo(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                        .build()
        );
    }

    @ExceptionHandler(CompensacionNoEncontradaException.class)
    public ResponseEntity<ApiErrorDto> handleNoEncontrada(CompensacionNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiErrorDto.builder()
                        .mensaje("Compensación no encontrada")
                        .detalle(ex.getMessage())
                        .codigo(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                        .build()
        );
    }


}