package com.coincraft.gestorFinanzas.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;


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

    @Column(nullable=false)
    private LocalDate fechaMovimiento;

    @ManyToOne @JoinColumn(name = "reto_origen_id", nullable = false)
    private Retos categoriaOrigen;

    @ManyToOne @JoinColumn(name = "reto_destino_id", nullable = false)
    private Retos categoriaDestino;

    @Column(nullable=false)
    private Double cantidad;
}
