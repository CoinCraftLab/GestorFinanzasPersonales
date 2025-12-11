package com.coincraft.gestorFinanzas.dto.retoDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/*
    Contiene los datos que envía el front para
    hacer un movimiento de dinero en un reto. Pantalla 1
 */
public record MovimientoRequest(
        @NotNull(message="Debe seleccionar la categoría de origen")
        String categoriaOrigen,

        @NotNull(message="Debe seleccionar la categoría de destino")
        String categoriaDestino,

        @NotNull(message="Debe introducir una cantidad")
        @Positive(message="La cantidad debe ser superior a 0")
        Double cantidad,

        //Si no se rellena, se usa la actual
        LocalDateTime fechaMovimiento
) {
}
