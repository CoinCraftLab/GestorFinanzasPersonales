package com.coincraft.gestorFinanzas.controller.WEB;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;


@Controller("WebInversionController")
@RequiredArgsConstructor
public class WebInversionController {

    //Pantalla principal (Portfolio). Pantalla 1
    @GetMapping("/inversiones")
    public String getToInversiones(Model model) {
        return "public/inversiones/inversiones";
    }

    //Pantalla de crear inversión. Pantalla 2
    @GetMapping("/inversiones/crear")
    public String getToCrearInversion(Model model) {
        return "public/inversiones/crear";
    }

    //Pantalla de vender inversión. Pantalla 3
    @GetMapping("/inversiones/vender")
    public String getToVenderInversion(Model model) {
        return "public/inversiones/vender";
    }

    //Pantalla de historial. Pantalla 4
    @GetMapping("/inversiones/historial")
    public String getToHistorial(Model model) {
        return "public/inversiones/historial";
    }
    
}
