package com.example.compensaciones.domain.service;

import com.example.compensaciones.client.dto.ExchangeRateApiResponseDto;
import com.example.compensaciones.config.FxApiProperties;
import com.example.compensaciones.domain.exception.TipoCambioNoDisponibleException;
import com.example.compensaciones.domain.service.impl.FxServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FxServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FxApiProperties fxApiProperties;

    @InjectMocks
    private FxServiceImpl fxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(fxApiProperties.getUrl()).thenReturn("https://fake-api.com");
        when(fxApiProperties.getKey()).thenReturn("12345");
    }

    @Test
    void obtenerTipoCambio_devuelveTipoCambioCorrecto() {
        // Arrange
        ExchangeRateApiResponseDto mockResponse = new ExchangeRateApiResponseDto();
        mockResponse.setConversion_rates(Map.of("COP", 3900.50));
        String expectedUrl = "https://fake-api.com/12345/latest/USD";

        when(restTemplate.getForObject(eq(expectedUrl), eq(ExchangeRateApiResponseDto.class)))
                .thenReturn(mockResponse);

        BigDecimal result = fxService.obtenerTipoCambio("usd", "cop");

        assertThat(result).isEqualByComparingTo("3900.50");
    }

    @Test
    void obtenerTipoCambio_monedaNoEncontrada_lanzaExcepcion() {
        ExchangeRateApiResponseDto mockResponse = new ExchangeRateApiResponseDto();
        mockResponse.setConversion_rates(Map.of("EUR", 1.1));

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateApiResponseDto.class)))
                .thenReturn(mockResponse);

        assertThatThrownBy(() -> fxService.obtenerTipoCambio("USD", "COP"))
                .isInstanceOf(TipoCambioNoDisponibleException.class)
                .hasMessageContaining("USD")
                .hasMessageContaining("COP");
    }

    @Test
    void obtenerTipoCambio_respuestaNull_lanzaRuntimeException() {
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateApiResponseDto.class)))
                .thenReturn(null);

        assertThatThrownBy(() -> fxService.obtenerTipoCambio("USD", "COP"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No se pudo obtener el tipo de cambio");
    }
}
