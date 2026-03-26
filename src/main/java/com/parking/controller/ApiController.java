package com.parking.controller;

import com.parking.model.Celda;
import com.parking.model.Cliente;
import com.parking.model.Registro;
import com.parking.model.Vehiculo;
import com.parking.repository.*;
import com.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CeldaRepository celdaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ParkingService parkingService;

    // Endpoint de diagnóstico
    @GetMapping("/diagnostico")
    public Map<String, Object> diagnostico() {
        Map<String, Object> resultado = new HashMap<>();
        try {
            long totalCeldas = celdaRepository.count();
            List<Celda> celdas = celdaRepository.findAll();

            resultado.put("success", true);
            resultado.put("totalCeldas", totalCeldas);
            resultado.put("celdas", celdas);
            resultado.put("mensaje", "Conexión a BD exitosa");
        } catch (Exception e) {
            resultado.put("success", false);
            resultado.put("error", e.getMessage());
        }
        return resultado;
    }

    // Endpoint para inicializar celdas
    @GetMapping("/inicializar-celdas")
    public String inicializarCeldas() {
        String[] numeros = {"A01", "A02", "A03", "A04", "B01", "B02", "B03", "B04", "C01", "C02", "C03", "C04"};
        String[] ubicaciones = {
                "Zona A - Nivel 1", "Zona A - Nivel 1", "Zona A - Nivel 1", "Zona A - Nivel 1",
                "Zona B - Nivel 1", "Zona B - Nivel 1", "Zona B - Nivel 2", "Zona B - Nivel 2",
                "Zona C - Nivel 2", "Zona C - Nivel 2", "Zona C - Nivel 2", "Zona C - Nivel 2"
        };

        int count = 0;
        for (int i = 0; i < numeros.length; i++) {
            if (!celdaRepository.existsByNumeroCelda(numeros[i])) {
                Celda celda = new Celda();
                celda.setNumeroCelda(numeros[i]);
                celda.setUbicacion(ubicaciones[i]);
                celda.setEstado("disponible");
                celda.setTipoVehiculo("auto");
                celdaRepository.save(celda);
                count++;
            }
        }

        return "Se inicializaron " + count + " celdas nuevas. Total: " + celdaRepository.count();
    }

    @GetMapping("/celdas/disponibles")
    public List<Celda> getCeldasDisponibles() {
        System.out.println("=== API: /api/celdas/disponibles ===");
        List<Celda> celdas = celdaRepository.findByEstadoOrderByNumeroCelda("disponible");
        System.out.println("Celdas disponibles encontradas: " + celdas.size());
        for (Celda c : celdas) {
            System.out.println("  - " + c.getNumeroCelda() + ": " + c.getEstado());
        }
        return celdas;
    }

    @GetMapping("/celdas")
    public List<Celda> getAllCeldas() {
        System.out.println("=== API: /api/celdas ===");
        List<Celda> celdas = celdaRepository.findAll();
        System.out.println("Total celdas: " + celdas.size());
        return celdas;
    }

    @GetMapping("/clientes/mensualidad")
    public List<Cliente> getClientesMensualidad() {
        System.out.println("=== API: /api/clientes/mensualidad ===");
        return clienteRepository.findClientesMensualidadActivos();
    }

    @GetMapping("/clientes")
    public List<Cliente> getAllClientes() {
        return clienteRepository.findByEstadoTrueOrderByFechaRegistroDesc();
    }

    @GetMapping("/vehiculos/activos")
    public List<Map<String, Object>> getVehiculosActivos() {
        return parkingService.getVehiculosActivos();
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = parkingService.getDashboardStats();
        stats.put("totalCeldasEnBD", celdaRepository.count());
        return stats;
    }
}