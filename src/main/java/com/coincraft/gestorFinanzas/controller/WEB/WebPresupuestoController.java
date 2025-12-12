package com.coincraft.gestorFinanzas.controller.WEB;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;


@Controller("WebPresupuestoController")
@RequiredArgsConstructor
public class WebPresupuestoController {

    @GetMapping("/presupuestos")
    public String getToPresupuestos(Model model) {
        model.addAttribute("mensaje", "Texto plano desde el controller (presupuestos)");
        return "public/presupuestos/presupuestos";
    }

    
}
