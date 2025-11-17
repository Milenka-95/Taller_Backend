package com.taller.modiesel.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String nombre;
    private String marca;
    private int cantidad;
    private double precio;
    private double precioUnitario;
    private String tipoMovimiento;
    private String descripcionMovimiento;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}
