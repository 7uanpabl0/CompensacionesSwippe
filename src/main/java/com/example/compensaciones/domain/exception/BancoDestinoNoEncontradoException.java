package com.example.compensaciones.domain.exception;

public class BancoDestinoNoEncontradoException  extends RuntimeException {
    public BancoDestinoNoEncontradoException(String moneda) {
        super("No se encontró un banco para la moneda destino: " + moneda);
    }
}
