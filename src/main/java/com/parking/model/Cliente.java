package com.parking.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_cliente", nullable = false)
    private String tipoCliente;

    private String identificacion;

    @Column(nullable = false)
    private String nombre;

    private String telefono;
    private String direccion;
    private String email;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "fecha_vencimiento_mensualidad")
    private LocalDate fechaVencimientoMensualidad;

    private Boolean estado = true;

    public Cliente() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDate getFechaVencimientoMensualidad() { return fechaVencimientoMensualidad; }
    public void setFechaVencimientoMensualidad(LocalDate fechaVencimientoMensualidad) {
        this.fechaVencimientoMensualidad = fechaVencimientoMensualidad;
    }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public boolean isMensualidadActiva() {
        if (!"mensualidad".equals(tipoCliente)) return false;
        if (fechaVencimientoMensualidad == null) return false;
        return LocalDate.now().isBefore(fechaVencimientoMensualidad);
    }
}