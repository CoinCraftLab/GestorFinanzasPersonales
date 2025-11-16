package com.coincraft.gestorFinanzas.controller.API;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coincraft.gestorFinanzas.dto.transactionDTO.CreateTransactionDTO;
import com.coincraft.gestorFinanzas.dto.transactionDTO.TransactionResponseDTO;
import com.coincraft.gestorFinanzas.dto.transactionDTO.UpdateTransactionDTO;
import com.coincraft.gestorFinanzas.service.TransactionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    //Inyección del servicio
    private final TransactionService transactionService;



    //1. CREAR TRANSACCION (POST)
    @PostMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> crearTransaccion(
            @RequestBody CreateTransactionDTO dto){

        TransactionResponseDTO response = transactionService.crearTransaccion(dto);
        return ResponseEntity.ok(response);
    }




    //2. OBTENER TRANSACCION POR ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> obtenerTransaccion(
            @PathVariable Long id){

        TransactionResponseDTO response = transactionService.obtenerTransaccion(id);
        return ResponseEntity.ok(response);
    }




    //3. EDITAR TRANSACCION (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> editarTransaccion(
            @PathVariable Long id, @RequestBody UpdateTransactionDTO dto){

        TransactionResponseDTO response = transactionService.editarTransaccion(id, dto);
        return ResponseEntity.ok(response);
    }




    //4. ELIMINAR TRANSACCION (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> eliminarTransaccion(
            @PathVariable Long id){

        transactionService.eliminarTransaccion(id);
        return ResponseEntity.noContent().build();

    }
        
    

}
