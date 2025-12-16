package com.coincraft.gestorFinanzas.controller.API;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coincraft.gestorFinanzas.dto.inversionDTO.InversionRequest;
import com.coincraft.gestorFinanzas.dto.inversionDTO.InversionResponse;
import com.coincraft.gestorFinanzas.dto.inversionDTO.PortfolioResponse;
import com.coincraft.gestorFinanzas.model.ActivoFinanciero;
import com.coincraft.gestorFinanzas.repository.ActivoFinancieroRepository;
import com.coincraft.gestorFinanzas.service.InversionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inversiones")
@RequiredArgsConstructor
public class InversionController {

    private final InversionService inversionService;
    private final ActivoFinancieroRepository activoFinancieroRepository;

    //Ruta para obtener el portfolio. Pantalla 1 y pantalla 4
    @GetMapping("/portfolio")
    public ResponseEntity<PortfolioResponse> getPortfolio(){
        return ResponseEntity.ok(inversionService.getPortfolio());
    }

    //Ruta para crear una inversion. Pantalla 2
    @PostMapping("/crear")
    public ResponseEntity<InversionResponse> crearCompra(@RequestBody @Valid InversionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inversionService.crearInversion(request));
    }

    //Ruta para vender o eliminar una inversión. Pantalla 3
    @PostMapping("/vender")
    public ResponseEntity<InversionResponse> venderActivo(@RequestBody @Valid InversionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inversionService.venderInversion(request));
    }

    //Ruta para mostrar el historial. Pantalla 4
    @GetMapping("/historial")
    public ResponseEntity <List<InversionResponse>> getHistorial(){
        List<InversionResponse> historial=inversionService.getHistorial();
        return ResponseEntity.ok(historial);
    }

    //Ruta para actualizar una inversion. Pantalla 5
    @PutMapping("/update/{id}")
    public ResponseEntity<InversionResponse> editarInversion(@PathVariable Long id, @RequestBody @Valid InversionRequest request){
        return ResponseEntity.ok(inversionService.editarInversion(id, request));
    }

    //Ruta para eliminar una inversion
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<InversionResponse> eliminarInversion(@PathVariable Long id) {
        InversionResponse eliminado=inversionService.eliminarInversion(id);
        return ResponseEntity.ok(eliminado);
    }

    //Ruta para obtener los activos de los selectores de compra/venta. Pantallas 2 y 3
    @GetMapping("/activos")
    public ResponseEntity<List<ActivoFinanciero>> getAllActivos(){
        return ResponseEntity.ok(activoFinancieroRepository.findAll());
    }
}

