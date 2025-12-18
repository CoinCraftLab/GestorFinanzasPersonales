package com.coincraft.gestorFinanzas.controller.WEB;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coincraft.gestorFinanzas.dto.retoDTO.MovimientoRequest;
import com.coincraft.gestorFinanzas.dto.retoDTO.RetoRequest;
import com.coincraft.gestorFinanzas.dto.retoDTO.RetoResumenDTO;
import com.coincraft.gestorFinanzas.service.RetoService;

import lombok.RequiredArgsConstructor;

@Controller("WebRetoController")
@RequiredArgsConstructor
public class WebRetoController {

    private final RetoService retoService;

    @GetMapping("/retos")
    public String getToRetos(Model model) {
        var lista = retoService.getListaRetos();

        double totalRetos = lista.stream()
                .mapToDouble(RetoResumenDTO::getRetoCantidadActual)
                .sum();

        double liquidez = lista.stream()
                .filter(r -> "Liquidez".equalsIgnoreCase(r.getNombre()))
                .mapToDouble(RetoResumenDTO::getRetoCantidadActual)
                .findFirst()
                .orElse(0.0);

        double ahorro = lista.stream()
                .filter(r -> "Ahorro".equalsIgnoreCase(r.getNombre()))
                .mapToDouble(RetoResumenDTO::getRetoCantidadActual)
                .findFirst()
                .orElse(0.0);

        model.addAttribute("listaRetos", lista);
        model.addAttribute("totalRetos", totalRetos);
        model.addAttribute("liquidez", liquidez);
        model.addAttribute("ahorro", ahorro);
        return "public/retos/retos";
    }

    @PostMapping("/retos/movimiento")
    public String crearMovimiento(@ModelAttribute MovimientoRequest request) {
        retoService.crearMovimiento(request);

        return "redirect:/retos";
    }

    @GetMapping("/retos/crearReto")
    public String goToCrearReto(Model model) {
        model.addAttribute("listaRetos", retoService.getListaRetos());
        return "public/retos/crearReto";
    }

    @PostMapping("/retos/crearReto")
    public String crearReto(@ModelAttribute RetoRequest request) {
        retoService.crearReto(request);
        return "redirect:/retos";
    }

    @GetMapping("/retos/editarReto/{id}")
    public String formEditarReto(@PathVariable Long id, Model model) {
        model.addAttribute("reto", retoService.getDetalleReto(id));
        model.addAttribute("listaRetos", retoService.getListaRetos());
        return "public/retos/editarReto";
    }

    @PostMapping("/retos/editarReto/{id}")
    public String editarReto(@PathVariable Long id, @ModelAttribute RetoRequest request) {
        retoService.editarReto(id, request);
        return "redirect:/retos";
    }

    @GetMapping("/retos/{id}")
    public String verReto(@PathVariable Long id, Model model) {
        model.addAttribute("detalleReto", retoService.getDetalleReto(id));
        return "public/retos/verReto";
    }

}
