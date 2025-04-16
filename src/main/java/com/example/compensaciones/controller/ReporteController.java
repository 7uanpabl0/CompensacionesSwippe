package com.example.compensaciones.controller;

import com.example.compensaciones.client.dto.ReporteDiarioDto;
import com.example.compensaciones.domain.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping(value = "/diario", produces = "text/csv")
    public ResponseEntity<byte[]> descargarCsv() {
        byte[] archivo = reporteService.generarReporteDiarioCsv();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reporte_diario.csv")
                .body(archivo);
    }

}
