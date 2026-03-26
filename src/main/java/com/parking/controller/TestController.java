package com.parking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class TestController {

    @GetMapping("/test-celdas")
    public List<Map<String, String>> testCeldas() {
        List<Map<String, String>> celdas = new ArrayList<>();
        Map<String, String> celda = new HashMap<>();
        celda.put("id", "1");
        celda.put("numeroCelda", "A01");
        celda.put("ubicacion", "Zona A");
        celda.put("estado", "disponible");
        celdas.add(celda);
        return celdas;
    }
}