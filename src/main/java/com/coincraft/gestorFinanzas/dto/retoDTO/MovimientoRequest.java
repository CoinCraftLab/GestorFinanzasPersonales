package com.coincraft.gestorFinanzas.dto.retoDTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/*
    Contiene los datos que envía el front para
    hacer un movimiento de dinero en un reto. Pantalla 1
 */
public record MovimientoRequest(
        @NotNull(message="Debe seleccionar la categoría de origen")
        Long categoriaOrigen,

        @NotNull(message="Debe seleccionar la categoría de destino")
        Long categoriaDestino,

        @NotNull(message="Debe introducir una cantidad")
        @Positive(message="La cantidad debe ser superior a 0")
        Double cantidad,

        //Si no se rellena, se usa la actual
        LocalDate fechaMovimiento
) {
}
