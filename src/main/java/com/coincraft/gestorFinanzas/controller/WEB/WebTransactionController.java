package com.coincraft.gestorFinanzas.controller.WEB;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;


@Controller("TransactionController")
@RequiredArgsConstructor
public class WebTransactionController {

    @GetMapping("/transacciones")
            public String getToTransacciones() {
                return "public/transacciones/transacciones";
            }

    
}
