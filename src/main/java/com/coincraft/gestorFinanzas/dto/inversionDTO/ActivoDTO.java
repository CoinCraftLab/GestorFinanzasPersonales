package com.coincraft.gestorFinanzas.dto.inversionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivoDTO {

    private Long activoId;
    private String simbolo;
    private Double cantidad;
    private Double valorInvertido;
    private Double valorActual;

    public Double getPorcentajePortfolio(Double balanceTotal) {
        if (balanceTotal == 0) return 0.0;
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
