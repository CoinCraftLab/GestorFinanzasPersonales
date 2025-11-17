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

    public String getPorcentajePortfolio(Double balanceTotal) {
        if (balanceTotal==0 || balanceTotal==null) return "0%";
        double porcentaje=(valorActual / balanceTotal) * 100;
        return String.format("%.2f%%", porcentaje);
    }

    public String getGananciaPerdida() {
        double ganancia=valorActual - valorInvertido;
        return String.format("%.2f\u20AC", ganancia);
    }

    public String getPorcentajeGananciaPerdida() {
        if (valorInvertido == 0) return "0%";
        double porcentaje=((valorActual - valorInvertido) / valorInvertido) * 100;
        return String.format("%.2f%%", porcentaje);
    }

}
