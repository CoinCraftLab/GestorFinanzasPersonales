package com.coincraft.gestorFinanzas.dto.inversionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    - DTO utilizado para el metodo que muestra el portfolio. Pantalla 1
    - Se utiliza también para mostrar la tabla de la pantalla 4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivoResumenDTO {

    private Long activoId;
    private String ticker;
    private String nombre;
    private Double cantidad;
    private Double valorInvertido;
    private Double valorActual;

    public Double getPorcentajePortfolio(Double balanceTotal) {
        if (balanceTotal==0 || balanceTotal==null) return 0.0;
        return (valorActual / balanceTotal) * 100;
    }

    public Double getGananciaPerdida() {
        return valorActual - valorInvertido;
    }

    public Double getPorcentajeGananciaPerdida() {
        if (valorInvertido == 0) return 0.0;
        return ((valorActual - valorInvertido) / valorInvertido) * 100;
    }

}
