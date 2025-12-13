package com.coincraft.gestorFinanzas.dto.presupuestoDTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Este DTO representa la respuesta que enviamos al cliente
 * cuando consulta o crea/actualiza un presupuesto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresupuestoResponseDTO {

    private Long id;

    private Long userId;

    private Long categoriaTransferenciaId;

    // Para mostrar directamente el nombre de la categoría en el frontend
    private String categoriaTransferenciaName;

    private LocalDateTime fechaTransaccion;

    private Double cantidad;

    private String description;
}
