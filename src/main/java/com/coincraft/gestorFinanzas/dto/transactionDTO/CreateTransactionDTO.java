package com.coincraft.gestorFinanzas.dto.transactionDTO;

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
    private Long userId;
    private Long tipoTransferenciaId;
    private Long categoriaTransferenciaId;
    private String fechaTransaccion;//Luego se convertirá a a LocalDateTime en el service
    private Double cantidad;
    private String descripcion;
}
