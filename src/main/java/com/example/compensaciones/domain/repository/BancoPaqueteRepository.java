package com.example.compensaciones.domain.repository;

import com.example.compensaciones.domain.model.BancoPaquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BancoPaqueteRepository extends JpaRepository<BancoPaquete, Long> {


    boolean existsById(UUID paqueteId);
}
