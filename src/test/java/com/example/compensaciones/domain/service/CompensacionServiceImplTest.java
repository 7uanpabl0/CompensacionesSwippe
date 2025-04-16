package com.example.compensaciones.domain.service;

import com.example.compensaciones.domain.model.BancoDestino;
import com.example.compensaciones.domain.model.Compensacion;
import com.example.compensaciones.domain.repository.BancoDestinoRepository;
import com.example.compensaciones.domain.repository.CompensacionRepository;
import com.example.compensaciones.domain.service.impl.CompensacionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompensacionServiceImplTest {

    @Mock
    private CompensacionRepository compensacionRepository;

    @Mock
    private BancoDestinoRepository bancoDestinoRepository;

    @Mock
    private FxService fxService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CompensacionServiceImpl compensacionService;

    @Test
    void ejecutarCompensacion_Fallo_basesDatos() {
        // 🔸 Transacción simulada
        Compensacion tx = Compensacion.builder()
                .id(1L)
                .monto(new BigDecimal("100"))
                .monedaOrigen("USD")
                .monedaDestino("COP")
                .ejecutado(false)
                .build();

        when(compensacionRepository.findByEjecutadoFalse()).thenReturn(List.of(tx));
        when(bancoDestinoRepository.findByMoneda("COP"))
                .thenReturn(Optional.of(BancoDestino.builder()
                        .moneda("COP")
                        .endpoint("http://localhost:8081/api/bancolombia")
                        .build()));

        doThrow(new RuntimeException("Fallo en BD"))
                .when(compensacionRepository).saveAll(any());

        assertThatCode(() -> compensacionService.ejecutarCompensacion())
                .doesNotThrowAnyException();

        verify(compensacionRepository).saveAll(any());
    }

    @Test
    void ejecutarCompensacion_noEjecutaTransaccionesDuplicacion() {

        Compensacion yaEjecutada = Compensacion.builder()
                .id(1L)
                .monto(new BigDecimal("100"))
                .monedaOrigen("USD")
                .monedaDestino("COP")
                .ejecutado(true)
                .build();

        when(compensacionRepository.findByEjecutadoFalse())
                .thenReturn(List.of());

        assertThatCode(() -> compensacionService.ejecutarCompensacion())
                .doesNotThrowAnyException();

        verify(compensacionRepository, never()).saveAll(any());
        verify(restTemplate, never()).postForObject(anyString(), any(), any());
    }
}