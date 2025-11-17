package com.taller.modiesel.dto;

import java.time.LocalDateTime;

public class FacturaDTO {
    private Long id;
    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private double montoTotal;
    private Long ventaId;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {return numeroFactura;}

    public void setNumeroFactura(String numeroFactura) {this.numeroFactura = numeroFactura;}

    public LocalDateTime getFechaEmision() {return fechaEmision;}

    public void setFechaEmision(LocalDateTime fechaEmision) {this.fechaEmision = fechaEmision;}

    public double getMontoTotal() {return montoTotal;}

    public void setMontoTotal(double montoTotal) {this.montoTotal = montoTotal;}

    public Long getVentaId() {return ventaId;}

    public void setVentaId(Long ventaId) {this.ventaId = ventaId;}
}
