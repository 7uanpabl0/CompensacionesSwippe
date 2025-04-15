package com.example.compensaciones.domain.service.impl;

import com.example.compensaciones.application.dto.CompensacionRequestDto;
import com.example.compensaciones.application.dto.CompensacionResponseDto;
import com.example.compensaciones.domain.model.Compensacion;
import com.example.compensaciones.domain.repository.CompensacionRepository;
import com.example.compensaciones.domain.service.CompensacionService;
import com.example.compensaciones.domain.service.FxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompensacionServiceImpl implements CompensacionService {

    private final CompensacionRepository repository;
    private final FxService fxService;

    @Override
    public CompensacionResponseDto registrarCompensacion(CompensacionRequestDto requestDto) {
        BigDecimal tipoCambio = fxService.obtenerTipoCambio(requestDto.getMonedaOrigen(), requestDto.getMonedaDestino());
        BigDecimal montoConvertido = requestDto.getMonto().multiply(tipoCambio);
        System.out.println(requestDto.getMonto());
        System.out.println(tipoCambio);
        System.out.println(montoConvertido);

        Compensacion entity = Compensacion.builder()
                .monto(requestDto.getMonto())
                .monedaOrigen(requestDto.getMonedaOrigen())
                .monedaDestino(requestDto.getMonedaDestino())
                .tipoCambio(tipoCambio)
                .montoConvertido(montoConvertido)
                .fechaRegistro(LocalDateTime.now())
                .ejecutado(false)
                .build();

        Compensacion saved = repository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public Optional<CompensacionResponseDto> obtenerCompensacionPorId(Long id) {
        return repository.findById(id).map(this::mapToDto);
    }

    @Override
    public CompensacionResponseDto ejecutarCompensacion() {
       return null;

    }

    public BigDecimal calcularMontoConvertido(BigDecimal montoOriginal, String monedaOrigen, String monedaDestino) {
        BigDecimal tipoCambio = fxService.obtenerTipoCambio(monedaOrigen, monedaDestino);
        return montoOriginal.multiply(tipoCambio);
    }



    private CompensacionResponseDto mapToDto(Compensacion c) {
        return CompensacionResponseDto.builder()
                .id(c.getId())
                .monto(c.getMonto())
                .monedaOrigen(c.getMonedaOrigen())
                .monedaDestino(c.getMonedaDestino())
                .tipoCambio(c.getTipoCambio())
                .montoConvertido(c.getMontoConvertido())
                .fechaRegistro(c.getFechaRegistro())
                .ejecutado(c.isEjecutado())
                .build();
    }


}
