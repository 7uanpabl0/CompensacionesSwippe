package com.example.compensaciones.domain.exception;

public class CompensacionNoEncontradaException extends RuntimeException {
    public CompensacionNoEncontradaException(Long id) {
        super("No se encontró una compensación con ID: " + id);
    }
}
