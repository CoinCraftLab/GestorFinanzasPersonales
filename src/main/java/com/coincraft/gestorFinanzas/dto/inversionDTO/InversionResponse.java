package com.coincraft.gestorFinanzas.dto.inversionDTO;

import com.coincraft.gestorFinanzas.model.Inversion;

import java.time.LocalDateTime;

/*
    - DTO para la respuesta al metodo crearInversion() Pantalla 2.
    - Sirve también para el metodo que muestra el historial. Pantalla 4
    - Sirve para el metodo que edita una inversión. Pantalla 5
 */
public record InversionResponse(
        Long id,
        Long userId,
        Long activoFinancieroId,
        String ticker,
        String nombreCompleto,
        String categoria,
        LocalDateTime fechaTransaccion,
        Double cantidad,
        Double precio,
        Boolean tipo, // true = Compra, false = Venta
        String tipoTexto,
        Double valorTotal
) {

    public static InversionResponse fromEntity(Inversion inversion){
        return new InversionResponse(
                inversion.getId(),
                inversion.getUser().getId(),
                inversion.getActivoFinanciero().getId(),
                inversion.getActivoFinanciero().getTicker(),
                inversion.getActivoFinanciero().getNombreCompleto(),
                inversion.getActivoFinanciero().getCategoriaActivoFinanciero().getNombre(),
                inversion.getFechaTransaccion(),
                inversion.getCantidad(),
                inversion.getPrecio(),
                inversion.getTipo(),
                inversion.getTipo() ? "Compra" : "Venta",
                inversion.getCantidad() / inversion.getPrecio()
        );
    }

}
