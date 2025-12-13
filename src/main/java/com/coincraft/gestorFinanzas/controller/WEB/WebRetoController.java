package com.coincraft.gestorFinanzas.controller.WEB;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;


@Controller("WebRetoController")
@RequiredArgsConstructor
public class WebRetoController {

    @GetMapping("/retos")
    public String getToRetos(Model model) {
        model.addAttribute("mensaje", "Texto plano desde el controller (retos)");
        return "public/retos/retos";
    }

    
}
