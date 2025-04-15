package com.example.compensaciones.domain.exception;

public class TipoCambioNoDisponibleException extends RuntimeException {
    public TipoCambioNoDisponibleException(String monedaOrigen, String monedaDestino) {
        super("Tipo de cambio no disponible para: " + monedaOrigen + " -> " + monedaDestino);
    }
}
