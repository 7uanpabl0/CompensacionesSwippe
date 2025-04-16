package com.example.compensaciones.domain.service;


import com.example.compensaciones.client.dto.ReporteDiarioDto;

import java.util.List;

public interface ReporteService {
    List<ReporteDiarioDto> generarReporteDiario();

    byte[] generarReporteDiarioCsv();
}
