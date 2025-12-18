package com.coincraft.gestorFinanzas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coincraft.gestorFinanzas.dto.presupuestoDTO.PresupuestoRequestDTO;
import com.coincraft.gestorFinanzas.dto.presupuestoDTO.PresupuestoResponseDTO;
import com.coincraft.gestorFinanzas.model.CategoriaTransferencia;
import com.coincraft.gestorFinanzas.model.Presupuesto;
import com.coincraft.gestorFinanzas.model.User;
import com.coincraft.gestorFinanzas.repository.CategoriaTransferenciaRepository;
import com.coincraft.gestorFinanzas.repository.PresupuestoRepository;
import com.coincraft.gestorFinanzas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/*
 * Capa de servicio para la entidad Presupuesto.
 * Aquí escribimos la lógica de negocio y usamos los repositorios
 * para hablar con la base de datos.
 */

@Service
@RequiredArgsConstructor
public class PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final UserRepository userRepository;
    private final CategoriaTransferenciaRepository categoriaTransferenciaRepository;

    // Crear un nuevo presupuesto
    public PresupuestoResponseDTO crearPresupuesto(PresupuestoRequestDTO dto) {
        // 1. Buscamos el usuario al que pertenece este presupuesto
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Buscamos la categoría de transferencia asociada
        CategoriaTransferencia categoria = categoriaTransferenciaRepository
                .findById(dto.getCategoriaTransferenciaId())
                .orElseThrow(() -> new RuntimeException("Categoría de transferencia no encontrada"));

        // 3. Si no nos pasan fecha, ponemos la fecha actual
        LocalDateTime fecha = dto.getFechaTransaccion() != null ? dto.getFechaTransaccion() : LocalDateTime.now();

        // 4. Convertimos el DTO de entrada a la entidad Presupuesto
        Presupuesto presupuesto = Presupuesto.builder()
                .user_id(user)
                .categoriaTransferencia(categoria)
                .fechaTransaccion(fecha)
                .cantidad(dto.getCantidad())
                .description(dto.getDescription())
                .build();

        // 5. Guardamos en la BBDD
        Presupuesto guardado = presupuestoRepository.save(presupuesto);

        // 6. Devolvemos un DTO de respuesta
        return convertirAResponseDTO(guardado);
    }

    // Obtener un presupuesto por ID
    public PresupuestoResponseDTO obtenerPorId(Long id) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));

        return convertirAResponseDTO(presupuesto);
    }

    // Obtener todos los presupuestos de un usuario concreto
    public List<PresupuestoResponseDTO> obtenerPorUsuario(Long userId) {
        List<Presupuesto> presupuestos = presupuestoRepository.findByUserId(userId);

        return presupuestos.stream().map(this::convertirAResponseDTO).collect(Collectors.toList());
    }

    // Obtener todos los presupuestos
    public List<PresupuestoResponseDTO> obtenerTodos() {
        return presupuestoRepository.findAll().stream()
                .map(this::convertirAResponseDTO).collect(Collectors.toList());
    }

    // Actualizar un presupuesto existente
    public PresupuestoResponseDTO actualizarPresupuesto(Long id, PresupuestoRequestDTO dto) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));

        // Actualizar solo los campos que nos lleguen en el DTO
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            presupuesto.setUser_id(user);
        }

        if (dto.getCategoriaTransferenciaId() != null) {
            CategoriaTransferencia categoria = categoriaTransferenciaRepository
                    .findById(dto.getCategoriaTransferenciaId())
                    .orElseThrow(() -> new RuntimeException("Categoría de transferencia no encontrada"));

            // ✅ CORRECCIÓN: asignar la categoría encontrada al presupuesto
            presupuesto.setCategoriaTransferencia(categoria);
        }

        if (dto.getFechaTransaccion() != null) {
            presupuesto.setFechaTransaccion(dto.getFechaTransaccion());
        }

        if (dto.getCantidad() != null) {
            presupuesto.setCantidad(dto.getCantidad());
        }

        if (dto.getDescription() != null) {
            presupuesto.setDescription(dto.getDescription());
        }

        Presupuesto actualizado = presupuestoRepository.save(presupuesto);

        return convertirAResponseDTO(actualizado);
    }

    // Eliminar un presupuesto
    public void eliminarPresupuesto(Long id) {
        if (!presupuestoRepository.existsById(id)) {
            throw new RuntimeException("Presupuesto no encontrado");
        }

        presupuestoRepository.deleteById(id);
    }

    // Convertir la entidad Presupuesto a un DTO de respuesta
    private PresupuestoResponseDTO convertirAResponseDTO(Presupuesto presupuesto) {

        return PresupuestoResponseDTO.builder()
                .id(presupuesto.getId())
                .userId(presupuesto.getUser_id().getId())
                .categoriaTransferenciaId(presupuesto.getCategoriaTransferencia().getId())
                .categoriaTransferenciaName(presupuesto.getCategoriaTransferencia().getName())
                .fechaTransaccion(presupuesto.getFechaTransaccion())
                .cantidad(presupuesto.getCantidad())
                .description(presupuesto.getDescription())
                .build();
    }
}