package com.coincraft.gestorFinanzas.controller.WEB;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller("WebTransactionController")
@RequiredArgsConstructor
public class WebTransactionController {


    @GetMapping("/transacciones")
    public String getToTransacciones(Model model) {
            
        model.addAttribute("mensaje", "Texto plano desde el controller (Transacciones)");
        return "public/transacciones/transacciones";
    }


}
