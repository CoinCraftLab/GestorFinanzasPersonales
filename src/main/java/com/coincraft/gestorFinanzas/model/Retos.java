package com.coincraft.gestorFinanzas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Retos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Retos{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private double retoCantidad;

    @Column(nullable = false)
    private double retoCantidadActual;

    @Column(nullable=true)
    private LocalDateTime fechaFin;

    @Column(nullable=false)
    private boolean predefinido;

}
