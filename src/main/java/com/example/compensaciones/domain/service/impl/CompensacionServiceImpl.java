package com.example.compensaciones.domain.service.impl;

import com.example.compensaciones.application.dto.CompensacionRequestDto;
import com.example.compensaciones.application.dto.CompensacionResponseDto;
import com.example.compensaciones.domain.model.Compensacion;
import com.example.compensaciones.domain.repository.CompensacionRepository;
import com.example.compensaciones.domain.service.CompensacionService;
import com.example.compensaciones.domain.service.FxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompensacionServiceImpl implements CompensacionService {

    private final CompensacionRepository repository;
    private final FxService fxService;

    @Override
    @Transactional
    public CompensacionResponseDto registrarCompensacion(CompensacionRequestDto requestDto) {
        BigDecimal tipoCambio = fxService.obtenerTipoCambio(requestDto.getMonedaOrigen(), requestDto.getMonedaDestino()).setScale(6, RoundingMode.HALF_UP);;
        BigDecimal montoConvertido = requestDto.getMonto().multiply(tipoCambio).setScale(2, RoundingMode.HALF_UP);;

        Compensacion entity = Compensacion.builder()
                .monto(requestDto.getMonto())
                .monedaOrigen(requestDto.getMonedaOrigen())
                .monedaDestino(requestDto.getMonedaDestino())
                .tipoCambio(tipoCambio)
                .montoConvertido(montoConvertido)
                .ejecutado(false)
                .build();
        log.info("[REGISTRO] TXID={} MONTO={} ORIGEN={} DESTINO={} FX={}",
                entity.getMonto(),
                entity.getMonedaOrigen(),
                entity.getMonedaDestino(),
                entity.getTipoCambio()
        );
        Compensacion saved = repository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public Optional<CompensacionResponseDto> obtenerCompensacionPorId(Long id) {
        return repository.findById(id).map(this::mapToDto);
    }

    @Override
 //   @Transactional
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
                .ejecutado(c.isEjecutado())
                .build();
    }


}
