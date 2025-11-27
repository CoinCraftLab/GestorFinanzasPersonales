package com.coincraft.gestorFinanzas.dto.retoDTO;

import com.coincraft.gestorFinanzas.model.MovimientoReto;

import java.time.LocalDateTime;

/*
    Contiene el formato de respuesta que recibe el
    controlador de movimiento de un reto. Pantalla 1
 */
public record MovimientoResponse(
        Long id,
        Long retoId,
        LocalDateTime fechaMovimiento,
        String categoriaOrigen,
        String categoriaDestino,
        Double cantidad
) {
        public static MovimientoResponse fromEntity(MovimientoReto movimiento){
                return new MovimientoResponse(
                        movimiento.getId(),
                        movimiento.getReto().getId(),
                        movimiento.getFechaMovimiento(),
                        movimiento.getCategoriaOrigen(),
                        movimiento.getCategoriaDestino(),
                        movimiento.getCantidad()
                );
        }
}
