package com.taller.modiesel.dto;

public class InventarioDTO {
    private Long id;

    private String codigo;
    private String nombre;
    private String marca;
    private int cantidad;
    private double precio;
    private double precioUnitario;
    private String tipoMovimiento;
    private String descripcionMovimiento;

    // Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getCodigo() {return codigo;}

    public void setCodigo(String codigo) {this.codigo = codigo;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getMarca() {return marca;}

    public void setMarca(String marca) {this.marca = marca;}

    public int getCantidad() {return cantidad;}

    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

    public double getPrecio() {return precio;}

    public void setPrecio(double precio) {this.precio = precio;}

    public double getPrecioUnitario() {return precioUnitario;}

    public void setPrecioUnitario(double precioUnitario) {this.precioUnitario = precioUnitario;}

    public String getTipoMovimiento() {return tipoMovimiento;}

    public void setTipoMovimiento(String tipoMovimiento) {this.tipoMovimiento = tipoMovimiento;}

    public String getDescripcionMovimiento() {return descripcionMovimiento;}

    public void setDescripcionMovimiento(String descripcionMovimiento) {this.descripcionMovimiento = descripcionMovimiento;}
}
