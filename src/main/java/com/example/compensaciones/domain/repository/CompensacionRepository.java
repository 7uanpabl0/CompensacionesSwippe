package com.example.compensaciones.domain.repository;

import com.example.compensaciones.domain.model.Compensacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CompensacionRepository extends JpaRepository<Compensacion, Long> {


    List<Compensacion> findByEjecutadoFalse();

    List<Compensacion> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);


  //  @Query("SELECT c.id AS id, c.monto AS monto, c.monedaOrigen AS monedaOrigen, c.monedaDestino AS monedaDestino, c.fechaRegistro AS fechaRegistro, c.ejecutado AS ejecutado FROM Compensacion c WHERE c.ejecutado = true")
   // List<CompensacionResumenProjection> listarEjecutadas();

}
