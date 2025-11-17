package com.taller.modiesel.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String marca;
    private String descripcion;
    private double precio;
    private int stock;
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}