package com.coincraft.gestorFinanzas.service;

import com.coincraft.gestorFinanzas.dto.retoDTO.*;
import com.coincraft.gestorFinanzas.model.MovimientoReto;
import com.coincraft.gestorFinanzas.model.Retos;
import com.coincraft.gestorFinanzas.model.User;
import com.coincraft.gestorFinanzas.repository.MovimientoRepository;
import com.coincraft.gestorFinanzas.repository.RetoRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetoService {

    private final UserRepository userRepository;
    private final RetoRepository retoRepository;
    private final MovimientoRepository movimientoRepository;

    //Autenticación del usuario
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails details)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return userRepository.findByEmail(details.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    //Crea una lista con todos los retos. Pantalla 1
    public List<RetoResumenDTO> getListaRetos(){
        User user=getAuthenticatedUser();

        //Crea una lista con los retos del repositorio
        List<Retos> retos=retoRepository.findByUserId(user.getId());

        //Recorre la lista, extrae los datos y la muestra
        return retos.stream()
                .map(reto -> new RetoResumenDTO(
                        reto.getId(),
                        reto.getNombre(),
                        reto.getRetoCantidad(),
                        reto.getRetoCantidadActual(),
                        reto.getFechaFin(),
                        reto.isPredefinido()
                ))
                .collect(Collectors.toList());
    }

    //Crea un movimiento de dinero. Pantalla 1
    public MovimientoResponse crearMovimiento(Long retoId, MovimientoRequest request){
        User user=getAuthenticatedUser();

        //Busca el reto y comprueba que exista
        Retos reto=retoRepository.findById(retoId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        //Comprueba que el reto pertenece al usuario
        if (!reto.getUser_id().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para modificar este reto");
        }

        //Recoge la fecha. Si no se ha especificado, guarda la fecha actual
        LocalDateTime fecha = request.fechaMovimiento() != null
                ? request.fechaMovimiento()
                : LocalDateTime.now();

        //Crea un objeto con los datos recogidos en la request
        MovimientoReto movimiento = MovimientoReto.builder()
                .reto(reto)
                .fechaMovimiento(fecha)
                .categoriaOrigen(request.categoriaOrigen())
                .categoriaDestino(request.categoriaDestino())
                .cantidad(request.cantidad())
                .build();

        //Actualiza la cantidad actual del reto si el destino es el reto
        if (request.categoriaDestino().equals(reto.getNombre())) {
            reto.setRetoCantidadActual(reto.getRetoCantidadActual() + request.cantidad());
            retoRepository.save(reto);
        }

        //Guarda el movimiento en el repositorio
        MovimientoReto saved = movimientoRepository.save(movimiento);
        //Convierte el objeto en el DTO de respuesta
        return MovimientoResponse.fromEntity(saved);

    }

    //Crear un reto. Pantalla 2
    public RetoResponse crearReto(RetoRequest request){
        User user=getAuthenticatedUser();

        //Crea objeto con los datos recogidos en la reques
        Retos reto=Retos.builder()
                .user_id(user)
                .nombre(request.nombre())
                .retoCantidad(request.retoCantidad())
                .retoCantidadActual(0.0)
                .fechaFin(request.fechaFin())
                //Si predefinido viene vacío, setea a false
                .predefinido(request.predefinido() != null ? request.predefinido() : false)
                .build();

        //Guarda el objeto en el repositorio
        Retos saved=retoRepository.save(reto);

        //Convierte el objeto en el DTO de respuesta
        return RetoResponse.fromEntity(saved);

    }

    //Obtener detalle de un reto. Pantalla 3
    public RetoDetalleDTO getDetalleReto(Long retoId) {
        User user = getAuthenticatedUser();

        //Busca el reto por id y comprueba que exista
        Retos reto = retoRepository.findById(retoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        //Comprueba que el reto pertenece al usuario
        if (!reto.getUser_id().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para ver este reto");
        }

        //Obtiene los movimientos del reto
        List<MovimientoResponse> movimientos = movimientoRepository.findByRetoId(retoId)
                .stream()
                .map(MovimientoResponse::fromEntity)
                .sorted((a, b) -> b.fechaMovimiento().compareTo(a.fechaMovimiento()))
                .collect(Collectors.toList());

        //Crea la respuesta que se muestra
        return new RetoDetalleDTO(
                reto.getId(),
                reto.getNombre(),
                reto.getRetoCantidad(),
                reto.getRetoCantidadActual(),
                reto.getFechaFin(),
                reto.isPredefinido(),
                movimientos
        );
    }

    //Edita un reto. Pantalla 4
    public RetoResponse editarReto(Long id, RetoRequest request) {
        User user = getAuthenticatedUser();

        //Busca el reto por id y comprueba que exista
        Retos reto = retoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        //Comprueba que el usuario puede modificar
        if (!reto.getUser_id().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para editar este reto");
        }

        //Setea los campos recibidos con la información de la request
        if (request.nombre() != null) {
            reto.setNombre(request.nombre());
        }
        if (request.retoCantidad() != null) {
            reto.setRetoCantidad(request.retoCantidad());
        }
        if (request.fechaFin() != null) {
            reto.setFechaFin(request.fechaFin());
        }
        if (request.predefinido() != null) {
            reto.setPredefinido(request.predefinido());
        }

        //Guarda los nuevos datos en el repositorio
        Retos actualizado = retoRepository.save(reto);
        //Convierte el objeto en el DTO de respuesta
        return RetoResponse.fromEntity(actualizado);
    }

    //Elimina un reto. Devuelve el reto eliminado
    public RetoResponse eliminarReto(Long id) {
        User user = getAuthenticatedUser();

        //Busca el reto por id y comprueba que exista
        Retos reto = retoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        //Comprueba que el usuario puede modificar
        if (!reto.getUser_id().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para eliminar este reto");
        }

        //Recupera el reto que se va a eliminar
        RetoResponse eliminado = RetoResponse.fromEntity(reto);
        //Elimina el reto del repositorio
        retoRepository.delete(reto);
        //Devuelve el reto eliminado
        return eliminado;
    }

}
