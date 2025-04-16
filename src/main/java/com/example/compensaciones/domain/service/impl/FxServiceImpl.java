package com.example.compensaciones.domain.service.impl;

import com.example.compensaciones.domain.exception.TipoCambioNoDisponibleException;
import com.example.compensaciones.domain.service.FxService;
import com.example.compensaciones.client.dto.ExchangeRateApiResponseDto;
import com.example.compensaciones.config.FxApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


/**
 * Implementación del servicio de tipo de cambio (FX).
 * Consulta la API externa (ExchangeRate-API) para obtener el tipo de cambio
 * entre dos monedas.
 */

@Service
@RequiredArgsConstructor
public class FxServiceImpl implements FxService {

    private final RestTemplate restTemplate;
    private final FxApiProperties fxApiProperties;


    /**
     * Obtiene el tipo de cambio entre dos monedas usando la API externa.
     *
     * @param monedaOrigen  código de la moneda de origen (ej. "USD").
     * @param monedaDestino código de la moneda destino (ej. "COP").
     * @return tipo de cambio como BigDecimal.
     * @throws TipoCambioNoDisponibleException si no se encuentra la tasa.
     * @throws RuntimeException si la API no responde correctamente.
     */

    @Override
    public BigDecimal obtenerTipoCambio(String monedaOrigen, String monedaDestino) {
        String url = String.format(
                "%s/%s/latest/%s",
                fxApiProperties.getUrl(),
                fxApiProperties.getKey(),
                monedaOrigen.toUpperCase()
        );

        ExchangeRateApiResponseDto response = restTemplate.getForObject(url, ExchangeRateApiResponseDto.class);

        if (response != null && response.getConversion_rates() != null) {
            Double tasa = response.getConversion_rates().get(monedaDestino.toUpperCase());
            if (tasa != null) {
                return BigDecimal.valueOf(tasa);
            }
            throw new TipoCambioNoDisponibleException(monedaOrigen, monedaDestino);
        }

        throw new RuntimeException("No se pudo obtener el tipo de cambio para " + monedaOrigen + " a " + monedaDestino);
    }
}
