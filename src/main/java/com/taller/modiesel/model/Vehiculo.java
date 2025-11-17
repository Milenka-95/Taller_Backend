package com.taller.modiesel.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;
    private String marca;
    private String modelo;
    private int año;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}