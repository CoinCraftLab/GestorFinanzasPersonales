package com.coincraft.gestorFinanzas.dto.transactionDTO;
import java.time.LocalDate;

import lombok.Data;

//Lo que el cliente envía para EDITAR una transacción
@Data
public class UpdateTransactionDTO {
    private Long tipoTransferenciaId;
    private Long categoriaTransferenciaId;
    private LocalDate fechaTransaccion;
    private Double cantidad;
    private String descripcion;
}
