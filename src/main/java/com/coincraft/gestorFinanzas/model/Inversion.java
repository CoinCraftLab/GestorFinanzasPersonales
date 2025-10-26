package com.coincraft.gestorFinanzas.model;

import java.time.LocalDateTime;

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
@Table(name = "inversiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="activo_financiero_id",nullable=false)
    private ActivoFinanciero activoFinanciero;

    @Column(nullable=false)
    private LocalDateTime fechaTransaccion;

    @Column(nullable=false)
    private Double cantidad;

    @Column(nullable=false)
    private Double precio;

    @Column(nullable=true)
    private Boolean tipo; // True = Compra // False = Venta

}



