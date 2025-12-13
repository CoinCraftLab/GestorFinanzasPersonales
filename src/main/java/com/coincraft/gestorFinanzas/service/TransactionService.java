package com.coincraft.gestorFinanzas.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.coincraft.gestorFinanzas.dto.transactionDTO.CreateTransactionDTO;
import com.coincraft.gestorFinanzas.dto.transactionDTO.TransactionResponseDTO;
import com.coincraft.gestorFinanzas.dto.transactionDTO.UpdateTransactionDTO;
import com.coincraft.gestorFinanzas.model.CategoriaTransferencia;
import com.coincraft.gestorFinanzas.model.TipoTransferencia;
import com.coincraft.gestorFinanzas.model.Transaccion;
import com.coincraft.gestorFinanzas.model.User;
import com.coincraft.gestorFinanzas.repository.CategoriaTransferenciaRepository;
import com.coincraft.gestorFinanzas.repository.TipoTransferenciaRepository;
import com.coincraft.gestorFinanzas.repository.TransactionRepository;
import com.coincraft.gestorFinanzas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {


    // Repositorios necesarios para buscar datos en la BD
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TipoTransferenciaRepository tipoTransferenciaRepository;
    private final CategoriaTransferenciaRepository categoriaTransferenciaRepository;

    //1. CREAR TRANSACCIÓN
    public TransactionResponseDTO crearTransaccion(CreateTransactionDTO dto){

        //Buscar el usuario
        User user = getAuthenticatedUser();

        //Buscar el tipo de transferencia
        TipoTransferencia tipo = tipoTransferenciaRepository.findById(dto.getTipoTransferenciaId())
            .orElseThrow(() -> new RuntimeException("Tipo de transferencia no encontrado"));

        //Buscar la categoría de la transferencia
        CategoriaTransferencia categoria = categoriaTransferenciaRepository.findById(
            dto.getCategoriaTransferenciaId()).orElseThrow(() -> new RuntimeException(
                "Categoría no encontrada"));

        //Convertir la fecha de String a LocalDateTime
        LocalDateTime fecha = LocalDateTime.parse(dto.getFechaTransaccion());

        //Crear la entidad transaccion
        Transaccion transaccion = Transaccion.builder()
                .user(user)
                .tipoTransferencia(tipo)
                .categoriaTransferencia(categoria)
                .fechaTransaccion(fecha)
                .cantidad(dto.getCantidad())
                .description(dto.getDescripcion())
                .build();

        //Guardar en la BD
        Transaccion transGuardada = transactionRepository.save(transaccion);

        //Convertir a DTO de respuesta
        return convertirAResponseDTO(transGuardada);
    }




    //2. OBTENER TRANSACCIÓN
    public TransactionResponseDTO obtenerTransaccion(Long id){
        Transaccion transaccion = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        return convertirAResponseDTO(transaccion);
    }




    //3. EDITAR LA TRANSACCIÓN
    public TransactionResponseDTO editarTransaccion(Long id, UpdateTransactionDTO dto){

        //Buscar la transaccion
        Transaccion transaccion = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        //Actualizar tipo
        if(dto.getTipoTransferenciaId() != null){
            TipoTransferencia tipo = tipoTransferenciaRepository.findById(dto.getTipoTransferenciaId())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));
            transaccion.setTipoTransferencia(tipo);
        }

        //Actualizar categoria
        if(dto.getCategoriaTransferenciaId() != null){
            CategoriaTransferencia categoria = categoriaTransferenciaRepository.findById(
                dto.getCategoriaTransferenciaId()).orElseThrow(() -> new RuntimeException(
                    "Categoría no encontrada"));
            transaccion.setCategoriaTransferencia(categoria);
        }

        //Actualizar fecha
        if (dto.getFechaTransaccion() != null) {
            LocalDateTime fecha = LocalDateTime.parse(dto.getFechaTransaccion());
            transaccion.setFechaTransaccion(fecha);
        }

        //Actualizar cantidad
        if (dto.getCantidad() != null) {
            transaccion.setCantidad(dto.getCantidad());
        }

        //Actualizar descripción
        if (dto.getDescripcion() != null) {
            transaccion.setDescription(dto.getDescripcion());
        }

        //Guardar actualización
        Transaccion actualizada = transactionRepository.save(transaccion);

        return convertirAResponseDTO(actualizada);

    }




    //4. ELIMINAR TRANSACCIÓN
    public void eliminarTransaccion(Long id){
        if(!transactionRepository.existsById(id)){
            throw new RuntimeException("Transacción no encontrada");
        }
        transactionRepository.deleteById(id);
    }




    //Método auxiliar: convierte una entidad en DTO
    private TransactionResponseDTO convertirAResponseDTO(Transaccion t) {
        TransactionResponseDTO dto = new TransactionResponseDTO();

        dto.setId(t.getId());
        dto.setUserId(t.getUser().getId());
        dto.setTipoTransferenciaId(t.getTipoTransferencia().getId());
        dto.setTipoTransferenciaNombre(t.getTipoTransferencia().getName());  // suponiendo que tiene getNombre()
        dto.setCategoriaTransferenciaId(t.getCategoriaTransferencia().getId());
        dto.setCategoriaTransferenciaNombre(t.getCategoriaTransferencia().getName());
        dto.setFechaTransaccion(t.getFechaTransaccion().toString());
        dto.setCantidad(t.getCantidad());
        dto.setDescripcion(t.getDescription());

        return dto;
    }

    //Metodo para devolver al user autenticado 
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails details)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return userRepository.findByEmail(details.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
