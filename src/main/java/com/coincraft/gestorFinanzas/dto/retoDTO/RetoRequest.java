package com.coincraft.gestorFinanzas.dto.retoDTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/*
    - Contiene los datos que envía el front.
    - Sirve para crear reto.
    - Pantalla 2(Compra)
 */
public record RetoRequest(
        @NotNull(message="Debe introducir un nombre para el reto")
        String nombre,

        @NotNull(message="Debe introducir una cantidad como objetivo")
        @Positive(message="La cantidad debe ser superior a 0")
        Double retoCantidad,

        @NotNull(message="Debe seleccionar una fecha como objetivo")
        LocalDate fechaFin,

        Boolean predefinido
) {
}
