package com.parking.repository;

import com.parking.model.Celda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CeldaRepository extends JpaRepository<Celda, Long> {

    List<Celda> findByEstadoOrderByNumeroCelda(String estado);

    long countByEstado(String estado);

    boolean existsByNumeroCelda(String numeroCelda);
}