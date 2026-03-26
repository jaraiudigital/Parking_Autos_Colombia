package com.parking.repository;

import com.parking.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;  // ← IMPORTANTE: agregar este import

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByTipoClienteAndEstadoTrue(String tipoCliente);

    List<Cliente> findByEstadoTrueOrderByFechaRegistroDesc();

    Optional<Cliente> findByIdentificacion(String identificacion);  // ← Optional necesita import

    @Query("SELECT c FROM Cliente c WHERE c.tipoCliente = 'mensualidad' AND c.estado = true AND c.fechaVencimientoMensualidad > CURRENT_DATE")
    List<Cliente> findClientesMensualidadActivos();
}