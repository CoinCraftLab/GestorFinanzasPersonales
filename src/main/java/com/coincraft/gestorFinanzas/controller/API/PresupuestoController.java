package com.coincraft.gestorFinanzas.controller.API;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coincraft.gestorFinanzas.dto.presupuestoDTO.PresupuestoRequestDTO;
import com.coincraft.gestorFinanzas.dto.presupuestoDTO.PresupuestoResponseDTO;
import com.coincraft.gestorFinanzas.service.PresupuestoService;

import lombok.RequiredArgsConstructor;

/*
 * Controlador REST para la entidad Presupuesto.
 * Recibe las peticiones HTTP, delega en el servicio y devuelve respuestas.
 */

@RestController
@RequestMapping("/api/presupuestos")
@RequiredArgsConstructor
public class PresupuestoController {
    private final PresupuestoService presupuestoService;

    //Crear un nuevo presupuesto
    @PostMapping
    public ResponseEntity<PresupuestoResponseDTO> crearPresupuesto(@RequestBody PresupuestoRequestDTO dto){
        PresupuestoResponseDTO creado = presupuestoService.crearPresupuesto(dto);

        return ResponseEntity.ok(creado);
    }

    //Obtener un presupuesto por su Id
    @GetMapping("/{id}")
    public ResponseEntity<PresupuestoResponseDTO> obtenerPorId(@PathVariable Long id){
        PresupuestoResponseDTO presupuesto = presupuestoService.obtenerPorId(id);

        return ResponseEntity.ok(presupuesto);
    }

    //Obtener todos los presupuestos
    @GetMapping
    public ResponseEntity<List<PresupuestoResponseDTO>> obtenerTodos(){

        return ResponseEntity.ok(presupuestoService.obtenerTodos());
    }

    //Obtener todos los presupuestos de un usuario concreto
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<PresupuestoResponseDTO>> obtenerPorUsuario(@PathVariable Long userId){

        return ResponseEntity.ok(presupuestoService.obtenerPorUsuario(userId));
    }

    //Actualizar un presupuesto existente
    @PutMapping("/{id}")
    public ResponseEntity<PresupuestoResponseDTO> actualizarPresupuesto(@PathVariable Long id,
                        @RequestBody PresupuestoRequestDTO dto){
        
        PresupuestoResponseDTO actualizado = presupuestoService.actualizarPresupuesto(id, dto);

        return ResponseEntity.ok(actualizado);
    }

    //Eliminar un presupuesto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPresupuesto(@PathVariable Long id){
        presupuestoService.eliminarPresupuesto(id);

        return ResponseEntity.noContent().build();
    }

}
