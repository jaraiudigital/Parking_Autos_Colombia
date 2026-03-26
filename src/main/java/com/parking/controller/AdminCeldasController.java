package com.parking.controller;

import com.parking.model.Celda;
import com.parking.repository.CeldaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminCeldasController {

    @Autowired
    private CeldaRepository celdaRepository;

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
}
