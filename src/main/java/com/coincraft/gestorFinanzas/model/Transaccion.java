package com.coincraft.gestorFinanzas.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="tipo_id", nullable=false)
    private TipoTransferencia tipoTransferencia;

    @ManyToOne
    @JoinColumn(name="categoria_id", nullable=false)
    private CategoriaTransferencia categoriaTransferencia;

    @Column(nullable=false)
    private LocalDate fechaTransaccion;

    @Column(nullable=false)
    private Double cantidad;

    @Column(nullable=true)
    private String description;

}



