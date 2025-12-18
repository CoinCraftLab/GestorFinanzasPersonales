package com.coincraft.gestorFinanzas.dto.presupuestoDTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Este DTO representa los datos que el cliente (frontend) envía
 * cuando quiere crear o actualizar un presupuesto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresupuestoRequestDTO {

    // Id del usuario al que pertenece este presupuesto
    private Long userId;

    // Id de la categoría de transferencia asociada al presupuesto
    private Long categoriaTransferenciaId;

    // Momento en el que se aplica o registra este presupuesto
    // Si viene null, en el service pondremos la fecha actual
    private LocalDateTime fechaTransaccion;

    // Cantidad de dinero presupuestada
    private Double cantidad;

    // Descripción opcional del presupuesto
    private String description;

    private List<Long> categoriaIds;
    
}
