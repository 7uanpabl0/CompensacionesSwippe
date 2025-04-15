package com.example.compensaciones.domain.service;

import java.math.BigDecimal;

public interface FxService {
    BigDecimal obtenerTipoCambio(String monedaOrigen, String monedaDestino);
}
