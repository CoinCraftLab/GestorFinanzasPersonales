package com.coincraft.gestorFinanzas.controller.WEB;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coincraft.gestorFinanzas.dto.transactionDTO.CategoriaGastoDTO;
import com.coincraft.gestorFinanzas.dto.transactionDTO.CreateTransactionDTO;
import com.coincraft.gestorFinanzas.dto.transactionDTO.TransactionResponseDTO;
import com.coincraft.gestorFinanzas.dto.transactionDTO.UpdateTransactionDTO;
import com.coincraft.gestorFinanzas.repository.CategoriaTransferenciaRepository;
import com.coincraft.gestorFinanzas.repository.TipoTransferenciaRepository;
import com.coincraft.gestorFinanzas.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Controller("WebTransactionController")
@RequiredArgsConstructor
public class WebTransactionController {

    private final TipoTransferenciaRepository tipoTransferenciaRepository;
    private final CategoriaTransferenciaRepository categoriaTransferenciaRepository;
    private final TransactionService transactionService;

    @GetMapping("/transacciones")
    public String getToTransacciones(Model model) {

        model.addAttribute("mensaje", "Texto plano desde el controller (Transacciones)");
        model.addAttribute("tiposTransferencia", tipoTransferenciaRepository.findAll(Sort.by("name"))); // Lista Tipos
        model.addAttribute("categoriaTransferencia", categoriaTransferenciaRepository.findAll(Sort.by("name"))); // Lista Categorias
        model.addAttribute("historicoTransferencias", transactionService.listarHistorialUsuarioFijo()); // Lista transacciones
        return "public/transacciones/transacciones";
    }

    @PostMapping("/transacciones")
    public String postTransaccion(@ModelAttribute CreateTransactionDTO dto, RedirectAttributes ra) {
        transactionService.crearTransaccion(dto);
        return "redirect:/transacciones";
    }

    @GetMapping("/transacciones/edit/{id}") // Importante que el nombre empiece por transacciones
    public String getToEditTransacciones(@PathVariable Long id, Model model) {
        model.addAttribute("historicoTransferencias", transactionService.listarHistorialUsuarioFijo()); // Lista transacciones
        model.addAttribute("tiposTransferencia", tipoTransferenciaRepository.findAll(Sort.by("name"))); // Lista Tipos
        model.addAttribute("categoriaTransferencia", categoriaTransferenciaRepository.findAll(Sort.by("name"))); // Lista Categorias
        model.addAttribute("transaccion", transactionService.obtenerTransaccion(id)); //Datos transaccion Escojida
        return "public/transacciones/editTransacciones";
    }

    @PostMapping("/transacciones/edit/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute UpdateTransactionDTO dto) {
        transactionService.editarTransaccion(id, dto);
        return "redirect:/transacciones";
    }

    @PostMapping("/transacciones/edit/{id}/delete")
    public String eliminar(@PathVariable Long id) {
        transactionService.eliminarTransaccion(id);
        return "redirect:/transacciones";
    }

    @GetMapping("/presupuestos/prueba")
    public String getPresupuestosPrueba(
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "anio", required = false) Integer anio,
            Model model) {

        LocalDate hoy = LocalDate.now();
        int mesSel = (mes != null) ? mes : hoy.getMonthValue();
        int anioSel = (anio != null) ? anio : hoy.getYear();

        List<TransactionResponseDTO> gastos = transactionService.listarGastosPorMesAnio(mesSel, anioSel);
        List<CategoriaGastoDTO> categorias = transactionService.resumenGastosPorCategoria(mesSel, anioSel);

        model.addAttribute("historicoTransferencias", gastos);    
        model.addAttribute("categoriaCirculos", categorias); 
        model.addAttribute("mesSeleccionado", mesSel);
        model.addAttribute("anioSeleccionado", anioSel);

        return "public/presupuestos/presupuestosPrueba";
    }

}
