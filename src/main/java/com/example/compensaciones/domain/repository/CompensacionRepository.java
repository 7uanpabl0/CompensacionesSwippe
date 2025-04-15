package com.example.compensaciones.domain.repository;

import com.example.compensaciones.domain.model.Compensacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompensacionRepository extends JpaRepository<Compensacion, Long> {
}
