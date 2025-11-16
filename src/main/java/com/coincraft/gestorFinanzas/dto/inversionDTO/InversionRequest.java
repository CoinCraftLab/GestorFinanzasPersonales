package com.coincraft.gestorFinanzas.dto.inversionDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/*
    - Contiene los datos que envía el front.
    - Sirve para compra, venta y actualizacion porque el formulario
      tiene los mismos datos.
    - Pantallas 2, 3 y 5(Compra, ventay actualizacion)
 */
public record InversionRequest(
        @NotNull(message="Se debe seleccionar un activo financiero")
        String ticker,

        @NotNull(message="Debe introducir una cantidad")
        @Positive(message="La cantidad debe ser superior a 0")
        Double cantidad,

        @NotNull(message="Debe rellenar el precio de la inversión")
        @Positive(message="La cantidad debe ser superior a 0")
        Double precio,

        //Si no se rellena, se usa la actual
        LocalDateTime fechaTransaccion
) {
}
