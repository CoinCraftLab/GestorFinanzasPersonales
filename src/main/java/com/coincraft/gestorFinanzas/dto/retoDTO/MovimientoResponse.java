package com.coincraft.gestorFinanzas.dto.retoDTO;

import java.time.LocalDate;

import com.coincraft.gestorFinanzas.model.MovimientoReto;

/*
        Contiene el formato de respuesta que recibe el
        controlador de movimiento de un reto. Pantalla 1
 */
public record MovimientoResponse(
                Long id,
                LocalDate fechaMovimiento,
                Long categoriaOrigenId,
                String categoriaOrigenNombre,
                Long categoriaDestinoId,
                String categoriaDestinoNombre,
                Double cantidad) {
        public static MovimientoResponse fromEntity(MovimientoReto movimiento) {
                return new MovimientoResponse(
                                movimiento.getId(),
                                movimiento.getFechaMovimiento(),
                                movimiento.getCategoriaOrigen().getId(),
                                movimiento.getCategoriaOrigen().getNombre(),
                                movimiento.getCategoriaDestino().getId(),
                                movimiento.getCategoriaDestino().getNombre(),
                                movimiento.getCantidad());
        }
}
