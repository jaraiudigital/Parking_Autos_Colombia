package com.parking.service;

import com.parking.model.*;
import com.parking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ParkingService {

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private CeldaRepository celdaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Map<String, Object> registrarEntrada(Vehiculo vehiculo, Long celdaId, Long clienteId, Long usuarioId) {
        Map<String, Object> resultado = new HashMap<>();

        try {
            System.out.println("=== ParkingService.registrarEntrada ===");
            System.out.println("Vehículo: " + vehiculo.getPlaca());
            System.out.println("Celda ID: " + celdaId);

            // 1. Verificar si el vehículo ya existe
            Optional<Vehiculo> existingOpt = vehiculoRepository.findByPlaca(vehiculo.getPlaca());
            Long vehiculoId;

            if (existingOpt.isPresent()) {
                vehiculoId = existingOpt.get().getId();
                System.out.println("Vehículo ya existe, ID: " + vehiculoId);
            } else {
                vehiculo = vehiculoRepository.save(vehiculo);
                vehiculoId = vehiculo.getId();
                System.out.println("Nuevo vehículo creado, ID: " + vehiculoId);
            }

            // 2. Crear registro de entrada
            Registro registro = new Registro();
            registro.setVehiculoId(vehiculoId);
            registro.setCeldaId(celdaId);
            registro.setClienteId(clienteId);
            registro.setUsuarioSistemaId(usuarioId);
            registro.setFechaEntrada(LocalDateTime.now());
            registro.setEstado("activo");
            registro = registroRepository.save(registro);
            System.out.println("Registro creado, ID: " + registro.getId());

            // 3. Actualizar estado de la celda a OCUPADA
            Optional<Celda> celdaOpt = celdaRepository.findById(celdaId);
            if (celdaOpt.isPresent()) {
                Celda celda = celdaOpt.get();
                System.out.println("Celda antes: " + celda.getNumeroCelda() + " - " + celda.getEstado());
                celda.setEstado("ocupada");
                celdaRepository.save(celda);
                System.out.println("Celda después: " + celda.getNumeroCelda() + " - " + celda.getEstado());
            } else {
                System.out.println("ERROR: Celda no encontrada con ID: " + celdaId);
            }

            resultado.put("success", true);
            resultado.put("message", "Entrada registrada exitosamente");
            resultado.put("registroId", registro.getId());

        } catch (Exception e) {
            System.err.println("ERROR en registrarEntrada: " + e.getMessage());
            e.printStackTrace();
            resultado.put("success", false);
            resultado.put("message", "Error: " + e.getMessage());
        }

        return resultado;
    }

    @Transactional
    public Map<String, Object> registrarSalida(Long registroId, Double valorPagado, Long usuarioId) {
        Map<String, Object> resultado = new HashMap<>();

        try {
            System.out.println("=== ParkingService.registrarSalida ===");
            System.out.println("Registro ID: " + registroId);

            Optional<Registro> registroOpt = registroRepository.findById(registroId);
            if (registroOpt.isPresent()) {
                Registro registro = registroOpt.get();
                System.out.println("Registro encontrado - Vehículo ID: " + registro.getVehiculoId());

                registro.setFechaSalida(LocalDateTime.now());
                registro.setValorPagado(valorPagado);
                registro.setEstado("finalizado");

                long horas = ChronoUnit.HOURS.between(registro.getFechaEntrada(), registro.getFechaSalida());
                registro.setHorasEstadia((int) (horas == 0 ? 1 : horas));

                registroRepository.save(registro);
                System.out.println("Registro actualizado a finalizado");

                // Liberar celda
                Optional<Celda> celdaOpt = celdaRepository.findById(registro.getCeldaId());
                if (celdaOpt.isPresent()) {
                    Celda celda = celdaOpt.get();
                    System.out.println("Celda antes: " + celda.getNumeroCelda() + " - " + celda.getEstado());
                    celda.setEstado("disponible");
                    celdaRepository.save(celda);
                    System.out.println("Celda después: " + celda.getNumeroCelda() + " - " + celda.getEstado());
                }

                resultado.put("success", true);
                resultado.put("message", "Salida registrada exitosamente");
                resultado.put("valor", valorPagado);
            } else {
                resultado.put("success", false);
                resultado.put("message", "Registro no encontrado");
            }

        } catch (Exception e) {
            System.err.println("ERROR en registrarSalida: " + e.getMessage());
            e.printStackTrace();
            resultado.put("success", false);
            resultado.put("message", "Error: " + e.getMessage());
        }

        return resultado;
    }

    public List<Map<String, Object>> getVehiculosActivos() {
        List<Map<String, Object>> resultado = new ArrayList<>();
        List<Registro> registros = registroRepository.findByEstadoOrderByFechaEntradaDesc("activo");

        System.out.println("=== getVehiculosActivos ===");
        System.out.println("Registros activos encontrados: " + registros.size());

        for (Registro r : registros) {
            Optional<Vehiculo> vOpt = vehiculoRepository.findById(r.getVehiculoId());
            Optional<Celda> cOpt = celdaRepository.findById(r.getCeldaId());

            if (vOpt.isPresent() && cOpt.isPresent()) {
                Vehiculo v = vOpt.get();
                Celda c = cOpt.get();

                Map<String, Object> item = new HashMap<>();
                item.put("id", r.getId());
                item.put("placa", v.getPlaca());
                item.put("marca", v.getMarca());
                item.put("numeroCelda", c.getNumeroCelda());
                item.put("fechaEntrada", r.getFechaEntrada());

                long horas = ChronoUnit.HOURS.between(r.getFechaEntrada(), LocalDateTime.now());
                long minutos = ChronoUnit.MINUTES.between(r.getFechaEntrada(), LocalDateTime.now()) % 60;
                item.put("tiempo", horas + "h " + minutos + "m");

                resultado.add(item);
                System.out.println("  - " + v.getPlaca() + " en celda " + c.getNumeroCelda());
            }
        }
        return resultado;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("vehiculosActivos", registroRepository.countByEstado("activo"));
        stats.put("celdasDisponibles", celdaRepository.countByEstado("disponible"));
        stats.put("celdasOcupadas", celdaRepository.countByEstado("ocupada"));
        stats.put("totalCeldas", celdaRepository.count());
        stats.put("clientesMensualidadActivos", clienteRepository.findClientesMensualidadActivos().size());

        System.out.println("=== Stats ===");
        System.out.println("Vehículos activos: " + stats.get("vehiculosActivos"));
        System.out.println("Celdas disponibles: " + stats.get("celdasDisponibles"));
        System.out.println("Celdas ocupadas: " + stats.get("celdasOcupadas"));

        return stats;
    }
}
