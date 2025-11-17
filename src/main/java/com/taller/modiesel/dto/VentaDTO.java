package com.taller.modiesel.dto;

import java.time.LocalDateTime;
import java.util.List;

public class VentaDTO {
    private Long id;
    private LocalDateTime fecha;
    private double total;
    private Long clienteId;
    private Long empleadoId;
    private List<DetalleVentaDTO> detalles;

    // Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public LocalDateTime getFecha() {return fecha;}

    public void setFecha(LocalDateTime fecha) {this.fecha = fecha;}

    public double getTotal() {return total;}

    public void setTotal(double total) {this.total = total;}

    public Long getClienteId() {return clienteId;}

    public void setClienteId(Long clienteId) {this.clienteId = clienteId;}

    public Long getEmpleadoId() {return empleadoId;}

    public void setEmpleadoId(Long empleadoId) {this.empleadoId = empleadoId;}

    public List<DetalleVentaDTO> getDetalles() {return detalles;}

    public void setDetalles(List<DetalleVentaDTO> detalles) {this.detalles = detalles;}
}
