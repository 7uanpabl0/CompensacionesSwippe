package com.example.compensaciones.domain.exception;

public class TransaccionDuplicadaException extends RuntimeException {
    public TransaccionDuplicadaException(String id) {
        super("Ya existe una transacción con ID: " + id);
    }
}