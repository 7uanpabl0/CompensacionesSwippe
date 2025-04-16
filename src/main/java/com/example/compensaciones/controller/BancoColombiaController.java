package com.example.compensaciones.controller;

import com.example.compensaciones.client.dto.CompensacionPaqueteDto;
import com.example.compensaciones.domain.service.BancoPaqueteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/bancolombia")
@RequiredArgsConstructor
public class BancoColombiaController {


    private final BancoPaqueteService bancoPaqueteService;

    @PostMapping
    public ResponseEntity<Void> recibirPaquete(@RequestBody CompensacionPaqueteDto paquete) {
        log.info("Paquete recibido en bancolombia: {}", paquete);
        bancoPaqueteService.registrarPaquete(paquete);
        return ResponseEntity.ok().build(); // o .noContent().build()
    }
}
