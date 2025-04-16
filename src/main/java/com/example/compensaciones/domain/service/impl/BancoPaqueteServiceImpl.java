package com.example.compensaciones.domain.service.impl;

import com.example.compensaciones.application.dto.CompensacionResponseDto;
import com.example.compensaciones.client.dto.CompensacionPaqueteDto;
import com.example.compensaciones.domain.model.BancoPaquete;
import com.example.compensaciones.domain.model.BancoTransaccion;
import com.example.compensaciones.domain.model.EstadoPaquete;
import com.example.compensaciones.domain.repository.BancoPaqueteRepository;
import com.example.compensaciones.domain.repository.BancoTransaccionRepository;
import com.example.compensaciones.domain.service.BancoPaqueteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BancoPaqueteServiceImpl implements BancoPaqueteService {

    private final BancoPaqueteRepository bancoPaqueteRepository;
    private final BancoTransaccionRepository bancoTransaccionRepository;

    @Override
    public void registrarPaquete(CompensacionPaqueteDto dto) {
        // Verificar si el paquete ya ha sido registrado
        if (paqueteYaRegistrado(dto.getPaqueteId())) {
            log.info("El paquete con ID {} ya ha sido registrado previamente, evitando duplicados.", dto.getPaqueteId());
            return;  // Si ya fue registrado, no lo procesamos nuevamente
        }

        BigDecimal montoTotal = dto.getTransacciones().stream()
                .map(CompensacionResponseDto::getMontoConvertido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Generar un UUID si no está presente en el DTO
        UUID paqueteId = dto.getPaqueteId() != null ? dto.getPaqueteId() : UUID.randomUUID();

        BancoPaquete paquete = BancoPaquete.builder()
                .id(paqueteId)  // Establecer el ID único del paquete
                .fechaRecepcion(LocalDateTime.now())
                .estado(EstadoPaquete.PENDIENTE)
                .montoTotal(montoTotal)
                .build();

        List<BancoTransaccion> transacciones = dto.getTransacciones().stream()
                .map(tx -> BancoTransaccion.builder()
                        .monto(tx.getMonto())
                        .monedaOrigen(tx.getMonedaOrigen())
                        .monedaDestino(tx.getMonedaDestino())
                        .tipoCambio(tx.getTipoCambio())
                        .ejecutado(tx.isEjecutado())
                        .paquete(paqueteId)
                        .build()
                ).toList();

        paquete.setTransacciones(transacciones);

        bancoPaqueteRepository.save(paquete);  // Guardar el paquete de transacciones

        log.info("📦 Paquete recibido con {} transacciones. Total: ${}",
                transacciones.size(), montoTotal);
    }

    private boolean paqueteYaRegistrado(UUID paqueteId) {
        return bancoPaqueteRepository.existsById(paqueteId);
    }
}
