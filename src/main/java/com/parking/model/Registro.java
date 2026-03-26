package com.parking.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registros")
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehiculo_id", nullable = false)
    private Long vehiculoId;

    @Column(name = "celda_id", nullable = false)
    private Long celdaId;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "usuario_sistema_id")
    private Long usuarioSistemaId;

    @Column(name = "fecha_entrada")
    private LocalDateTime fechaEntrada = LocalDateTime.now();

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    @Column(name = "horas_estadia")
    private Integer horasEstadia;

    @Column(name = "valor_pagado")
    private Double valorPagado;

    @Column(name = "metodo_pago")
    private String metodoPago = "efectivo";

    private String estado = "activo";

    @Transient
    private String placa;

    @Transient
    private String marca;

    @Transient
    private String numeroCelda;

    public Registro() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(Long vehiculoId) { this.vehiculoId = vehiculoId; }

    public Long getCeldaId() { return celdaId; }
    public void setCeldaId(Long celdaId) { this.celdaId = celdaId; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getUsuarioSistemaId() { return usuarioSistemaId; }
    public void setUsuarioSistemaId(Long usuarioSistemaId) { this.usuarioSistemaId = usuarioSistemaId; }

    public LocalDateTime getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDateTime fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDateTime fechaSalida) { this.fechaSalida = fechaSalida; }

    public Integer getHorasEstadia() { return horasEstadia; }
    public void setHorasEstadia(Integer horasEstadia) { this.horasEstadia = horasEstadia; }

    public Double getValorPagado() { return valorPagado; }
    public void setValorPagado(Double valorPagado) { this.valorPagado = valorPagado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getNumeroCelda() { return numeroCelda; }
    public void setNumeroCelda(String numeroCelda) { this.numeroCelda = numeroCelda; }
}
