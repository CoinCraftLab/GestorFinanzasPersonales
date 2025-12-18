package com.coincraft.gestorFinanzas.dto.presupuestoDTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoriaGastoMesDTO {
    Long categoriaId;
    String categoriaNombre;
    Double presupuestoAsignado;
    Double gastadoMes;
    Integer pctUsado;
}
