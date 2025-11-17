package com.taller.modiesel.dto;

public class ProveedorDTO {
    private Long id;
    private String nombre;
    private String ruc;
    private String telefono;
    private String correo;
    private String direccion;

    // Getters y Setters
    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getRuc() {return ruc;}

    public void setRuc(String ruc) {this.ruc = ruc;}

    public String getTelefono() {return telefono;}

    public void setTelefono(String telefono) {this.telefono = telefono;}

    public String getCorreo() {return correo;}

    public void setCorreo(String correo) {this.correo = correo;}

    public String getDireccion() {return direccion;}

    public void setDireccion(String direccion) {this.direccion = direccion;}
}
