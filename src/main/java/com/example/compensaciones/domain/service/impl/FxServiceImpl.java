package com.example.compensaciones.domain.service.impl;

import com.example.compensaciones.domain.exception.TipoCambioNoDisponibleException;
import com.example.compensaciones.domain.service.FxService;
import com.example.compensaciones.client.dto.ExchangeRateApiResponseDto;
import com.example.compensaciones.config.FxApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FxServiceImpl implements FxService {

    private final RestTemplate restTemplate;
    private final FxApiProperties fxApiProperties;

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
