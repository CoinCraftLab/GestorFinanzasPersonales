package com.coincraft.gestorFinanzas.service;

import com.coincraft.gestorFinanzas.dto.inversionDTO.ActivoResumenDTO;
import com.coincraft.gestorFinanzas.dto.inversionDTO.InversionRequest;
import com.coincraft.gestorFinanzas.dto.inversionDTO.InversionResponse;
import com.coincraft.gestorFinanzas.dto.inversionDTO.PortfolioResponse;
import com.coincraft.gestorFinanzas.model.ActivoFinanciero;
import com.coincraft.gestorFinanzas.model.Inversion;
import com.coincraft.gestorFinanzas.model.User;
import com.coincraft.gestorFinanzas.repository.ActivoFinancieroRepository;
import com.coincraft.gestorFinanzas.repository.InversionRepository;
import com.coincraft.gestorFinanzas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InversionService {

    private final UserRepository userRepository;
    private final InversionRepository inversionRepository;
    private final ActivoFinancieroRepository activoFinancieroRepository;

    //Metodo para devolver al user autenticado
    /*
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails details)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return userRepository.findByEmail(details.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

     */
    //Metodo para devolver siempre el user 1
    private User getAuthenticatedUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Usuario fijo (id=1) no encontrado"));
    }

    //Calcular balance. Pantalla 1 y pantalla 4
    public PortfolioResponse getPortfolio() {
        User user = getAuthenticatedUser();
        List<Inversion> inversiones = inversionRepository.findByUserId(user.getId());

        //Calcula balance y lo agrupa por activos
        Map<Long, ActivoResumenDTO> activosMap = new HashMap<>();
        double balanceTotal = 0.0;

        for (Inversion inv : inversiones) {
            Long activoId = inv.getActivoFinanciero().getId();
            String ticker = inv.getActivoFinanciero().getTicker();
            String nombre = inv.getActivoFinanciero().getNombreCompleto();

            if (!activosMap.containsKey(activoId)) {
                activosMap.put(activoId, new ActivoResumenDTO(
                        activoId,
                        ticker,
                        nombre,
                        0.0,
                        0.0,
                        0.0
                ));
            }

            ActivoResumenDTO resumen = activosMap.get(activoId);
            double valorOperacion = inv.getCantidad() * inv.getPrecio();

            if (inv.getTipo()) { //Compra
                resumen.setCantidad(resumen.getCantidad() + inv.getCantidad());
                resumen.setValorInvertido(resumen.getValorInvertido() + valorOperacion);
            } else { //Venta
                resumen.setCantidad(resumen.getCantidad() - inv.getCantidad());
            }
        }

        for (ActivoResumenDTO resumen : activosMap.values()) {
            if (resumen.getCantidad() > 0) {
                //Obtiene el activo financiero para acceder al precio actual
                ActivoFinanciero activo = activoFinancieroRepository.findById(resumen.getActivoId())
                        .orElse(null);

                if (activo != null && activo.getValorActual() != null) {
                    //Usa el valorActual del activo
                    resumen.setValorActual(resumen.getCantidad() * activo.getValorActual());
                    balanceTotal += resumen.getValorActual();
                }
            }
        }

        List<ActivoResumenDTO> activos = activosMap.values().stream()
                .filter(a -> a.getCantidad() > 0)
                .collect(Collectors.toList());

        return new PortfolioResponse(balanceTotal, activos);
    }

    //Añadir inversión. Pantalla 2
    public InversionResponse crearInversion(InversionRequest request) {
        User user = getAuthenticatedUser();

        //Busca el activo por el ticker de la request. Si no existe, sale un aviso
        ActivoFinanciero activo = activoFinancieroRepository.findByTicker(request.ticker())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El activo no existe"));

        //Recoge la fecha. Si no se ha especificado, guarda la fecha actual
        LocalDateTime fecha = request.fechaTransaccion() != null
                ? request.fechaTransaccion()
                : LocalDateTime.now();

        //Crea un objeto con los datos recogidos en la request
        Inversion inversion = Inversion.builder()
                .user(user)
                .activoFinanciero(activo)
                .fechaTransaccion(fecha)
                .cantidad(request.cantidad())
                .precio(request.precio())
                .tipo(true) // true = Compra
                .build();

        //Guarda los datos en el repositorio
        Inversion saved = inversionRepository.save(inversion);
        //Convierte el objeto guardado (saved) en el DTO de respuesta
        return InversionResponse.fromEntity(saved);
    }

    //Eliminar inversión. Venta. Pantalla 3
    public InversionResponse venderInversion(InversionRequest request) {
        User user = getAuthenticatedUser();

        //Busca el activo por el ticker de la request. Si no existe, sale un aviso
        ActivoFinanciero activo = activoFinancieroRepository.findByTicker(request.ticker())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El activo no existe"));

        //Comprueba que hay tanta cantidad del activo como la que se quiere vender
        List<Inversion> inversiones = inversionRepository.findByUserIdAndActivoFinancieroId(user.getId(), activo.getId());
        double cantidadDisponible = 0.0;

        for (Inversion inv : inversiones) {
            if (inv.getTipo()) {
                cantidadDisponible += inv.getCantidad();
            } else {
                cantidadDisponible -= inv.getCantidad();
            }
        }

        if (cantidadDisponible < request.cantidad()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No tienes suficiente cantidad de este activo para vender");
        }

        //Asigna la fecha de la request. Si está vacío, aplica la fecha actual
        LocalDateTime fecha = request.fechaTransaccion() != null
                ? request.fechaTransaccion()
                : LocalDateTime.now();

        //Crea un objeto con los datos de la request
        Inversion inversion = Inversion.builder()
                .user(user)
                .activoFinanciero(activo)
                .fechaTransaccion(fecha)
                .cantidad(request.cantidad())
                .precio(request.precio())
                .tipo(false) // false = Venta
                .build();

        //Guarda los datos en el repositorio
        Inversion saved = inversionRepository.save(inversion);
        //Convierte el objeto en el DTO de respuesta
        return InversionResponse.fromEntity(saved);
    }

    //Obtener el historial de inversiones. Pantalla 4
    public List<InversionResponse> getHistorial() {
        User user = getAuthenticatedUser();

        //Agrupa en una lista las inversiones del id del usuario
        List<Inversion> inversiones = inversionRepository.findByUserId(user.getId());

        //Recorre la lista y las muestra con el formato del dto response en orden
        return inversiones.stream()
                .map(InversionResponse::fromEntity)
                .sorted((a, b) -> b.fechaTransaccion().compareTo(a.fechaTransaccion()))
                .collect(Collectors.toList());

    }

    //Editar una inversión. Pantalla 5
    public InversionResponse editarInversion(Long id, InversionRequest request) {
        User user = getAuthenticatedUser();

        //Busca la inversión por id y comprueba que exista. Si no existe, sale un aviso
        Inversion inversion = inversionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inversión no encontrada"));

        //Comprueba que el usuario puede modificar
        if (!inversion.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para editar esta inversión");
        }

        //Setea los campos recibidos con la información de la request
        if (request.cantidad() != null) {
            inversion.setCantidad(request.cantidad());
        }
        if (request.precio() != null) {
            inversion.setPrecio(request.precio());
        }
        if (request.fechaTransaccion() != null) {
            inversion.setFechaTransaccion(request.fechaTransaccion());
        }

        //Guarda los nuevos datos en el repositorio
        Inversion actualizada = inversionRepository.save(inversion);
        //Convierte el objeto en el DTO de respuesta
        return InversionResponse.fromEntity(actualizada);
    }

    //Eliminar un activo. Devuelve el activo eliminado
    public InversionResponse eliminarInversion(Long id){
        User user = getAuthenticatedUser();

        //Busca la inversión por id y comprueba que exista. Si no existe, sale un aviso
        Inversion inversion = inversionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inversión no encontrada"));

        //Comprueba que el usuario puede modificar
        if (!inversion.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para editar esta inversión");
        }

        //Recupera la inversion que se va a eliminar
        InversionResponse eliminado=InversionResponse.fromEntity(inversion);
        //Elimina la inversion del repositorio
        inversionRepository.delete(inversion);
        //Devuelve la inversión eliminada
        return eliminado;
    }
}