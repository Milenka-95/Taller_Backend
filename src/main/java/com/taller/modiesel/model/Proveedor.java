package com.taller.modiesel.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruc;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;
}
