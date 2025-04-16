package com.example.compensaciones.domain.service.impl;

import com.example.compensaciones.client.dto.ReporteDiarioDto;
import com.example.compensaciones.domain.model.Compensacion;
import com.example.compensaciones.domain.repository.CompensacionRepository;
import com.example.compensaciones.domain.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final CompensacionRepository repository;

    @Override
    public List<ReporteDiarioDto>
    generarReporteDiario() {
        LocalDate hoy = LocalDate.now(ZoneOffset.UTC);
        LocalDateTime desde = hoy.atStartOfDay();
        LocalDateTime hasta = hoy.plusDays(1).atStartOfDay();

        List<Compensacion> deHoy = repository.findByFechaRegistroBetween(desde, hasta);

        Map<String, List<Compensacion>> agrupadas = deHoy.stream()
                .collect(Collectors.groupingBy(Compensacion::getMonedaDestino));

        return agrupadas.entrySet().stream()
                .map(entry -> {
                    List<Compensacion> lista = entry.getValue();
                    String moneda = entry.getKey();
                    BigDecimal montoTotal = lista.stream()
                            .map(Compensacion::getMonto)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    long ejecutadas = lista.stream().filter(Compensacion::isEjecutado).count();
                    long noEjecutadas = lista.size() - ejecutadas;

                    return ReporteDiarioDto.builder()
                            .moneda(moneda)
                            .totalTransacciones(lista.size())
                            .ejecutadas((int) ejecutadas)
                            .noEjecutadas((int) noEjecutadas)
                            .montoTotal(montoTotal)
                            .build();
                })
                .toList();
    }

    @Override
    public byte[] generarReporteDiarioCsv() {
        List<ReporteDiarioDto> datos = generarReporteDiario();

        StringBuilder sb = new StringBuilder();
        sb.append("Moneda,Total Transacciones,Ejecutadas,No Ejecutadas,Monto Total\n");

        for (ReporteDiarioDto dto : datos) {
            sb.append(dto.getMoneda()).append(",");
            sb.append(dto.getTotalTransacciones()).append(",");
            sb.append(dto.getEjecutadas()).append(",");
            sb.append(dto.getNoEjecutadas()).append(",");
            sb.append(dto.getMontoTotal()).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
