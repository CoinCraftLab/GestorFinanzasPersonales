package com.coincraft.gestorFinanzas.dto.retoDTO;

import com.coincraft.gestorFinanzas.model.Retos;

import java.time.LocalDateTime;

/*
    - Respuesta para los métodos del cotrolador.
    - Crear. Pantalla 2
 */
public record RetoResponse (Long id,
                            Long userId,
                            String nombre,
                            Double retoCantidad,
                            Double retoCantidadActual,
                            LocalDateTime fechaFin,
                            Boolean predefinido) {

    public static RetoResponse fromEntity(Retos reto) {
        return new RetoResponse(
                reto.getId(),
                reto.getUser_id().getId(),
                reto.getNombre(),
                reto.getRetoCantidad(),
                reto.getRetoCantidadActual(),
                reto.getFechaFin(),
                reto.isPredefinido()
        );

    }


}
