package com.coincraft.gestorFinanzas.dto.retoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//DTO para mostrar la lista de los retos. Pantalla 1
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetoResumenDTO {

    private Long retoId;
    private String nombre;
    private Double retoCantidad;
    private Double retoCantidadActual;
    private LocalDateTime fechaFin;
    private Boolean predefinido;

    public Integer getPorcentajeCompletado() {
        if (retoCantidad == 0) return 0;
        double porcentaje = (retoCantidadActual / retoCantidad) * 100;
        return (int) Math.min(porcentaje, 100);
    }

    public String getCantidadFormateada() {
        return String.format("%.0f€/%.0f€", retoCantidadActual, retoCantidad);
    }

}
