package com.coincraft.gestorFinanzas.controller.WEB;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller("WebIndexController")
@RequiredArgsConstructor
public class WebIndexController {

    @GetMapping("/index")
    public String getToIndex(Model model) {
        model.addAttribute("mensaje", "Texto plano desde el controller (index)");
        return "public/index/index";
    }
}