package com.taller.modiesel.dto;

public class VehiculoDTO {
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private int año;
    private Long clienteId;

    // Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getPlaca() {return placa;}

    public void setPlaca(String placa) {this.placa = placa;}

    public String getMarca() {return marca;}

    public void setMarca(String marca) {this.marca = marca;}

    public String getModelo() {return modelo;}

    public void setModelo(String modelo) {this.modelo = modelo;}

    public int getAño() {return año;}

    public void setAño(int año) {this.año = año;}

    public Long getClienteId() {return clienteId;}

    public void setClienteId(Long clienteId) {this.clienteId = clienteId;}
}
