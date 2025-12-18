package com.coincraft.gestorFinanzas.dto.transactionDTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoriaGastoDTO {
    Long categoriaId;
    String categoriaNombre;
    Double total;
    Integer percent;
}
