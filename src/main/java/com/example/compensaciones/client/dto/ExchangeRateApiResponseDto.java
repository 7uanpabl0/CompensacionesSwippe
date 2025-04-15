package com.example.compensaciones.client.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRateApiResponseDto {
    private String base_code;
    private Map<String, Double> conversion_rates;
}
