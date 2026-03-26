package com.parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParkingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParkingApplication.class, args);
        System.out.println("========================================");
        System.out.println("  🏢 PARKING COLOMBIA");
        System.out.println("  Sistema iniciado correctamente");
        System.out.println("  Acceda a: http://localhost:8080");
        System.out.println("========================================");
    }
}