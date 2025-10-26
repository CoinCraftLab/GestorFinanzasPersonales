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
@Table(name = "activo_financiero")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivoFinanciero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,nullable=false)
    private String nombreCompleto;

    @Column(unique=true,nullable=false)
    private String ticker; // Esto es el acronimo con el que se abreria el nombre de un activo de BITCOIN A "BTC"

    @Column(nullable=false)
    private Double valorActual; // Valor actual registrado del producto 

    @Column(nullable=false)
    private LocalDateTime lastUpdated; //Aqui se registra la fecha en la que se ha modificado el valor

    @ManyToOne
    @JoinColumn(name="categoria_activo_financiero_id", nullable=false)
    private CategoriaActivoFinanciero categoriaActivoFinanciero;

    @OneToMany(mappedBy="activoFinanciero", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Inversion> inversion;

    @OneToMany(mappedBy="activoFinanciero", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<HistoricoValorActivoFinanciero> historicoValorActivoFinanciero;


}

