package com.parking.repository;

import com.parking.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;  // ← agregar este import

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {

    List<Registro> findByEstadoOrderByFechaEntradaDesc(String estado);

    @Query("SELECT r FROM Registro r WHERE r.estado = 'activo' AND r.vehiculoId IN " +
            "(SELECT v.id FROM Vehiculo v WHERE v.placa = :placa)")
    Optional<Registro> findRegistroActivoByPlaca(@Param("placa") String placa);  // ← Optional

    @Query("SELECT r FROM Registro r WHERE r.fechaEntrada BETWEEN :inicio AND :fin ORDER BY r.fechaEntrada DESC")
    List<Registro> findRegistrosByFechaRange(@Param("inicio") LocalDateTime inicio,
                                             @Param("fin") LocalDateTime fin);

    List<Registro> findByVehiculoIdOrderByFechaEntradaDesc(Long vehiculoId);

    @Query("SELECT COUNT(r) FROM Registro r WHERE r.estado = :estado")
    long countByEstado(@Param("estado") String estado);
}