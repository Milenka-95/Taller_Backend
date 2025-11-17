package com.taller.modiesel.dto;

import com.taller.modiesel.enums.Rol;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String correo;
    private Rol rol;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
