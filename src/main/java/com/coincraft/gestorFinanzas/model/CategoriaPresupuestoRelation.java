package com.coincraft.gestorFinanzas.model;



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
@Table(name = "categoriaPresupuestoRelation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoriaPresupuestoRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="presupuesto_id",nullable=false)
    private Presupuesto presupuesto;

    @ManyToOne
    @JoinColumn(name="categoria_id", nullable=false)
    private CategoriaTransferencia categoriaTransferencia;

}


