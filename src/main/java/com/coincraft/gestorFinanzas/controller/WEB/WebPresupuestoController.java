package com.coincraft.gestorFinanzas.controller.WEB;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coincraft.gestorFinanzas.dto.presupuestoDTO.PresupuestoRequestDTO;
import com.coincraft.gestorFinanzas.dto.presupuestoDTO.PresupuestoResponseDTO;
import com.coincraft.gestorFinanzas.dto.presupuestoDTO.PresupuestoWebDTO;
import com.coincraft.gestorFinanzas.repository.CategoriaTransferenciaRepository;
import com.coincraft.gestorFinanzas.repository.UserRepository;
import com.coincraft.gestorFinanzas.service.PresupuestoService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Controller("WebPresupuestoController")
@RequiredArgsConstructor
public class WebPresupuestoController {

    private static final Long DEFAULT_USER_ID = 1L;

    private final PresupuestoService presupuestoService;
    private final CategoriaTransferenciaRepository categoriaRepo;
    private final UserRepository userRepository;

    /*
     * =========================
     * VISTA PRINCIPAL
     * =========================
     */
    @GetMapping("/presupuestos")
    public String getPresupuestos(
            @RequestParam(value = "userId", required = false) Long userId,
            Model model) {

        Long effectiveUserId = (userId != null) ? userId : DEFAULT_USER_ID;

        // Para el selector de usuarios
        model.addAttribute("usuarios", userRepository.findAll(Sort.by("name")));

        // Para el selector de categorías (multiselección)
        model.addAttribute("categorias", categoriaRepo.findAll(Sort.by("name")));

        // Si no se selecciona usuario, dejamos lista vacía (así no mezclas usuarios en
        // pruebas)
        List<PresupuestoResponseDTO> presupuestos = presupuestoService.obtenerPorUsuario(effectiveUserId);

        model.addAttribute("selectedUserId", userId);
        model.addAttribute("ingresosAnuales", calcularBarrasAnuales(presupuestos));
        model.addAttribute("categoriaProgress", calcularProgresoCategorias(presupuestos));
        model.addAttribute("mensaje", "Presupuestos");

        return "public/presupuestos/presupuestos";
    }

    /*
     * =========================
     * CREAR PRESUPUESTO
     * =========================
     */
    @PostMapping("/presupuestos")
    public String crearPresupuesto(@ModelAttribute PresupuestoWebDTO webDto) {

        if (webDto.getUserId() == null) {
            throw new RuntimeException("Debe seleccionar un usuario");
        }
        if (webDto.getCantidadMensual() == null) {
            throw new RuntimeException("Cantidad mensual obligatoria");
        }
        if (webDto.getCategoriasAsociadasIds() == null || webDto.getCategoriasAsociadasIds().isEmpty()) {
            throw new RuntimeException("Debe seleccionar al menos una categoría");
        }

        // Creamos 1 Presupuesto por categoría seleccionada (sin tocar modelo)
        PresupuestoRequestDTO req = PresupuestoRequestDTO.builder()
                .userId(webDto.getUserId())
                .categoriaIds(webDto.getCategoriasAsociadasIds())
                .categoriaTransferenciaId(
                        (webDto.getCategoriasAsociadasIds() != null && !webDto.getCategoriasAsociadasIds().isEmpty())
                                ? webDto.getCategoriasAsociadasIds().get(0)
                                : null)
                .fechaTransaccion(LocalDateTime.now())
                .cantidad(webDto.getCantidadMensual())
                .description(buildDescription(webDto))
                .build();

        presupuestoService.crearPresupuesto(req);

        return "redirect:/presupuestos?userId=" + webDto.getUserId();
    }

    /*
     * =========================
     * PANTALLA DE EDICIÓN / LISTADO
     * =========================
     */
    @GetMapping("/presupuestos/edit")
    public String getEditPresupuestos(
            @RequestParam(value = "userId", required = false) Long userId,
            Model model) {

        Long effectiveUserId = (userId != null) ? userId : DEFAULT_USER_ID;

        model.addAttribute("usuarios", userRepository.findAll(Sort.by("name")));
        model.addAttribute("categorias", categoriaRepo.findAll(Sort.by("name")));
        model.addAttribute("selectedUserId", userId);

        List<PresupuestoResponseDTO> presupuestos = presupuestoService.obtenerPorUsuario(effectiveUserId);

        model.addAttribute("presupuestos", presupuestos);
        model.addAttribute("categoriaProgress", calcularProgresoCategorias(presupuestos));
        model.addAttribute("mensaje", "Editar Presupuestos");

        return "public/presupuestos/editPresupuestos";
    }

    /*
     * =========================
     * EDITAR (SUBMIT)
     * =========================
     */
    @PostMapping("/presupuestos/edit/{id}")
    public String actualizarPresupuesto(
            @PathVariable Long id,
            @ModelAttribute PresupuestoWebDTO webDto) {

        if (webDto.getUserId() == null) {
            throw new RuntimeException("Debe seleccionar un usuario");
        }

        Long categoriaId = null;
        if (webDto.getCategoriasAsociadasIds() != null && !webDto.getCategoriasAsociadasIds().isEmpty()) {
            // Para edición, usamos la primera categoría seleccionada (limitación del modelo
            // actual)
            categoriaId = webDto.getCategoriasAsociadasIds().get(0);
        }

        PresupuestoRequestDTO req = PresupuestoRequestDTO.builder()
                .userId(webDto.getUserId())
                .categoriaTransferenciaId(categoriaId)
                .fechaTransaccion(LocalDateTime.now())
                .cantidad(webDto.getCantidadMensual())
                .description(buildDescription(webDto))
                .build();

        presupuestoService.actualizarPresupuesto(id, req);

        return "redirect:/presupuestos/edit?userId=" + webDto.getUserId();
    }

    /*
     * =========================
     * ELIMINAR
     * =========================
     */
    @PostMapping("/presupuestos/edit/{id}/delete")
    public String eliminarPresupuesto(
            @PathVariable Long id,
            @RequestParam(value = "userId", required = false) Long userId) {

        presupuestoService.eliminarPresupuesto(id);

        if (userId != null) {
            return "redirect:/presupuestos/edit?userId=" + userId;
        }
        return "redirect:/presupuestos/edit";
    }

    // ============================
    // Helpers cálculo SOLO con presupuestos
    // ============================
    private List<BarraMes> calcularBarrasAnuales(List<PresupuestoResponseDTO> presupuestos) {

        Map<Month, Double> sumaPorMes = new EnumMap<>(Month.class);

        for (PresupuestoResponseDTO p : presupuestos) {
            if (p.getFechaTransaccion() == null || p.getCantidad() == null) {
                continue;
            }
            Month mes = p.getFechaTransaccion().getMonth();
            sumaPorMes.merge(mes, p.getCantidad(), Double::sum);
        }

        double max = sumaPorMes.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        if (max <= 0) {
            max = 1.0;
        }

        List<BarraMes> barras = new ArrayList<>();
        Locale es = new Locale("es", "ES");

        for (Month mes : Month.values()) {
            double cantidad = sumaPorMes.getOrDefault(mes, 0.0);
            int pct = (int) Math.round((cantidad / max) * 100);

            barras.add(new BarraMes(
                    mes.getDisplayName(TextStyle.SHORT, es).toLowerCase(),
                    cantidad,
                    pct));
        }
        return barras;
    }

    private List<CategoriaProgress> calcularProgresoCategorias(List<PresupuestoResponseDTO> presupuestos) {

        double totalTmp = presupuestos.stream()
                .filter(p -> p.getCantidad() != null)
                .mapToDouble(PresupuestoResponseDTO::getCantidad)
                .sum();

        final double total = (totalTmp <= 0) ? 0.0 : totalTmp;

        Map<String, Double> porCategoria = presupuestos.stream()
                .filter(p -> p.getCantidad() != null)
                .collect(Collectors.groupingBy(
                        p -> (p.getCategoriaTransferenciaName() == null ? "Sin categoría"
                                : p.getCategoriaTransferenciaName()),
                        Collectors.summingDouble(PresupuestoResponseDTO::getCantidad)));

        return porCategoria.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(e -> {
                    int pct = (total == 0.0) ? 0 : (int) Math.round((e.getValue() / total) * 100);
                    return new CategoriaProgress(e.getKey(), pct);
                })
                .toList();
    }

    private String buildDescription(PresupuestoWebDTO dto) {
        String nombre = dto.getNombrePresupuesto() == null ? "" : dto.getNombrePresupuesto().trim();
        String tipo = dto.getTipoPresupuesto() == null ? "" : dto.getTipoPresupuesto().trim();
        if (nombre.isBlank() && tipo.isBlank()) {
            return null;
        }
        if (!nombre.isBlank() && !tipo.isBlank()) {
            return nombre + " [" + tipo + "]";
        }
        return !nombre.isBlank() ? nombre : "[" + tipo + "]";
    }

    // ============================
    // Clases para la vista
    // ============================
    @Getter
    @AllArgsConstructor
    public static class BarraMes {

        private String mes;
        private Double cantidad;
        private Integer pct;
    }

    @Getter
    @AllArgsConstructor
    public static class CategoriaProgress {

        private String name;
        private Integer percent;
    }


    
}
