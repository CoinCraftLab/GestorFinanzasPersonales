package com.coincraft.gestorFinanzas.controller.API;

import com.coincraft.gestorFinanzas.dto.retoDTO.*;
import com.coincraft.gestorFinanzas.service.RetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/reto")
@RequiredArgsConstructor
public class RetoController {

    private final RetoService retoService;

    //Ver todos los retos. Pantalla 1
    @GetMapping("/listar")
    public ResponseEntity<List<RetoResumenDTO>> getListaRetos(){
        return ResponseEntity.ok(retoService.getListaRetos());
    }

    //Crea un movimiento de dinero. Pantalla 1
    @PostMapping("/{id}/movimiento")
    public ResponseEntity<MovimientoResponse> crearMovimiento(@PathVariable Long id, @RequestBody @Valid MovimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(retoService.crearMovimiento(id, request));
    }

    //Crear reto. Pantalla 2
    @PostMapping("/crear")
    public ResponseEntity<RetoResponse> crearReto(@RequestBody @Valid RetoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(retoService.crearReto(request));
    }

    //Obtiene los detalles de un reto. Pantalla 3
    @GetMapping("/{id}")
    public ResponseEntity<RetoDetalleDTO> getDetalleReto(@PathVariable Long id){
        return ResponseEntity.ok(retoService.getDetalleReto(id));
    }

    //Actualiza un reto. Pantalla 4
    @PutMapping("/update/{id}")
    public ResponseEntity<RetoResponse> editarReto(
            @PathVariable Long id,
            @RequestBody @Valid RetoRequest request) {
        return ResponseEntity.ok(retoService.editarReto(id, request));
    }

    //Elimina un reto
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<RetoResponse> eliminarReto(@PathVariable Long id) {
        RetoResponse eliminado = retoService.eliminarReto(id);
        return ResponseEntity.ok(eliminado);
    }

}
