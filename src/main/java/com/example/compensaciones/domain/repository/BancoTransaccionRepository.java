package com.example.compensaciones.domain.repository;

import com.example.compensaciones.domain.model.BancoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoTransaccionRepository extends JpaRepository<BancoTransaccion, Long> {
}