package com.coincraft.gestorFinanzas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_reto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoReto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="reto_id", nullable=false)
    private Retos reto;

    @Column(nullable=false)
    private LocalDateTime fechaMovimiento;

    @Column(nullable=false)
    private String categoriaOrigen;

    @Column(nullable=false)
    private String categoriaDestino;

    @Column(nullable=false)
    private Double cantidad;
}
