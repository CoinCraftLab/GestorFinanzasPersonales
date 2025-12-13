package com.coincraft.gestorFinanzas.controller.WEB;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;


@Controller("WebUserController")
@RequiredArgsConstructor
public class WebUserController {

    @GetMapping("/perfil")
    public String getToUser(Model model) {
        model.addAttribute("mensaje", "Texto plano desde el controller (perfil)");
        return "public/perfil/perfil";
    }

    
}
