package com.parking.model;

import javax.persistence.*;

@Entity
@Table(name = "celdas")
public class Celda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_celda", unique = true, nullable = false)
    private String numeroCelda;

    private String ubicacion;

    @Column(nullable = false)
    private String estado = "disponible";

    @Column(name = "tipo_vehiculo")
    private String tipoVehiculo = "auto";

    public Celda() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroCelda() { return numeroCelda; }
    public void setNumeroCelda(String numeroCelda) { this.numeroCelda = numeroCelda; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public boolean isDisponible() {
        return "disponible".equals(estado);
    }
}