package com.coincraft.gestorFinanzas.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "presupuestos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user_id;

    @ManyToOne
    @JoinColumn(name="categoria_id", nullable=false)
    private CategoriaTransferencia categoriaTransferencia;

    @Column(nullable=false)
    private LocalDateTime fechaTransaccion;

    @Column(nullable=false)
    private Double cantidad;

    @Column(nullable=true)
    private String description;

    @OneToMany(mappedBy="presupuesto", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<CategoriaPresupuestoRelation> categoriaPresupuestoRelations;

}



