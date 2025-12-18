package com.coincraft.gestorFinanzas.dto.presupuestoDTO;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresupuestoWebDTO {

    private Long userId;

    private String nombrePresupuesto;

    private Double cantidadMensual;

    private String tipoPresupuesto; // "GASTO" / "INGRESO"

    private List<Long> categoriasAsociadasIds;
}