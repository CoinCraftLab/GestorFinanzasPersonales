package com.coincraft.gestorFinanzas.dto.transactionDTO;

import java.time.LocalDate;

import lombok.Data;


/*  
    @Data: anotación en Lombok que genera automáticamente:

    - Getters y Setters
    - toString()
    - equals()
    - hashCode()
    - constructor vacío

 */


//Lo que el cliente envía para CREAR una transacción
@Data
public class CreateTransactionDTO {
    private Long tipoTransferenciaId;
    private Long categoriaTransferenciaId;
    private LocalDate fechaTransaccion;
    private Double cantidad;
    private String descripcion;
}
