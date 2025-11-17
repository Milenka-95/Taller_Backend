package com.taller.modiesel.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private LocalDateTime fechaEmision;
    private double total;

    @OneToOne
    @JoinColumn(name = "venta_id", unique = true)
    @JsonBackReference
    private Venta venta;
}
