package com.coincraft.gestorFinanzas.dto.transactionDTO;

import java.time.LocalDate;

import lombok.Data;


//Lo que DEVUELVES al cliente cuando pide una transacción
@Data
public class TransactionResponseDTO {
    private Long id;
    private Long userId;
    private Long tipoTransferenciaId;
    private String tipoTransferenciaNombre;
    private Long categoriaTransferenciaId;
    private String categoriaTransferenciaNombre;
    private LocalDate fechaTransaccion;
    private Double cantidad;
    private String descripcion;
}
