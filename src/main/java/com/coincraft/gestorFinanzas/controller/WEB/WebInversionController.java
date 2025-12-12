package com.coincraft.gestorFinanzas.controller.WEB;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;


@Controller("WebInversionController")
@RequiredArgsConstructor
public class WebInversionController {

    @GetMapping("/inversiones")
    public String getToInversiones(Model model) {
        model.addAttribute("mensaje", "Texto plano desde el controller (inversiones)");
        return "public/inversiones/inversiones";
    }

    
}
