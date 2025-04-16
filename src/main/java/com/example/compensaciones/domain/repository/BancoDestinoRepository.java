package com.example.compensaciones.domain.repository;

import com.example.compensaciones.domain.model.BancoDestino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoDestinoRepository extends JpaRepository<BancoDestino, Long> {
    Optional<BancoDestino> findByMoneda(String moneda);
}