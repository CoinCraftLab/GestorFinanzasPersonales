package com.coincraft.gestorFinanzas.dto.retoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/*
    Muestra la respuesta del metodo getDetalleReto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetoDetalleDTO {

    private Long retoId;
    private String nombre;
    private Double retoCantidad;
    private Double retoCantidadActual;
    private LocalDateTime fechaFin;
    private Boolean predefinido;
    private List<MovimientoResponse> movimientos;

    public Integer getPorcentajeCompletado() {
        if (retoCantidad == 0) return 0;
        double porcentaje = (retoCantidadActual / retoCantidad) * 100;
        return (int) Math.min(porcentaje, 100);
    }

    public Double getAporteMensualNecesario() {
        if (retoCantidad <= retoCantidadActual) return 0.0;

        LocalDateTime hoy = LocalDateTime.now();
        long mesesRestantes = ChronoUnit.MONTHS.between(hoy, fechaFin);

        if (mesesRestantes <= 0) return retoCantidad - retoCantidadActual;

        return (retoCantidad - retoCantidadActual) / mesesRestantes;
    }
}
