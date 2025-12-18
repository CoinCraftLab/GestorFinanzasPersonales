package com.coincraft.gestorFinanzas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.coincraft.gestorFinanzas.dto.retoDTO.MovimientoRequest;
import com.coincraft.gestorFinanzas.dto.retoDTO.MovimientoResponse;
import com.coincraft.gestorFinanzas.dto.retoDTO.RetoDetalleDTO;
import com.coincraft.gestorFinanzas.dto.retoDTO.RetoRequest;
import com.coincraft.gestorFinanzas.dto.retoDTO.RetoResponse;
import com.coincraft.gestorFinanzas.dto.retoDTO.RetoResumenDTO;
import com.coincraft.gestorFinanzas.model.MovimientoReto;
import com.coincraft.gestorFinanzas.model.Retos;
import com.coincraft.gestorFinanzas.model.User;
import com.coincraft.gestorFinanzas.repository.MovimientoRepository;
import com.coincraft.gestorFinanzas.repository.RetoRepository;
import com.coincraft.gestorFinanzas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetoService {

    private final UserRepository userRepository;
    private final RetoRepository retoRepository;
    private final MovimientoRepository movimientoRepository;

    private User getAuthenticatedUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario 1 no encontrado"));
    }

    public List<RetoResumenDTO> getListaRetos() {
        User user = getAuthenticatedUser();
        List<Retos> retos = retoRepository.findByUserId(user.getId());
        return retos.stream()
                .map(reto -> new RetoResumenDTO(
                        reto.getId(),
                        reto.getNombre(),
                        reto.getRetoCantidad(),
                        reto.getRetoCantidadActual(),
                        reto.getFechaFin(),
                        reto.isPredefinido()))
                .collect(Collectors.toList());
    }

    public MovimientoResponse crearMovimiento(MovimientoRequest request) {
        User user = getAuthenticatedUser();

        Retos origen = retoRepository.findById(request.categoriaOrigen())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto origen no encontrado"));
        Retos destino = retoRepository.findById(request.categoriaDestino())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto destino no encontrado"));

        if (!origen.getUser().getId().equals(user.getId()) || !destino.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para modificar estos retos");
        }

        LocalDate fecha = request.fechaMovimiento() != null ? request.fechaMovimiento() : LocalDate.now();

        MovimientoReto movimiento = MovimientoReto.builder()
                .categoriaOrigen(origen)
                .categoriaDestino(destino)
                .cantidad(request.cantidad())
                .fechaMovimiento(fecha)
                .build();

        origen.setRetoCantidadActual(origen.getRetoCantidadActual() - request.cantidad());
        destino.setRetoCantidadActual(destino.getRetoCantidadActual() + request.cantidad());
        retoRepository.save(origen);
        retoRepository.save(destino);

        MovimientoReto saved = movimientoRepository.save(movimiento);
        return MovimientoResponse.fromEntity(saved);
    }

    public RetoResponse crearReto(RetoRequest request) {
        User user = getAuthenticatedUser();

        Retos reto = Retos.builder()
                .user(user)
                .nombre(request.nombre())
                .retoCantidad(request.retoCantidad())
                .retoCantidadActual(0.0)
                .fechaFin(request.fechaFin().atStartOfDay())
                .predefinido(request.predefinido() != null ? request.predefinido() : false)
                .build();

        Retos saved = retoRepository.save(reto);
        return RetoResponse.fromEntity(saved);
    }

    public RetoDetalleDTO getDetalleReto(Long retoId) {
        User user = getAuthenticatedUser();

        Retos reto = retoRepository.findById(retoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        if (!reto.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para ver este reto");
        }

        List<MovimientoResponse> movimientos = movimientoRepository
                .findByCategoriaOrigenIdOrCategoriaDestinoId(retoId, retoId)
                .stream()
                .map(MovimientoResponse::fromEntity)
                .sorted((a, b) -> b.fechaMovimiento().compareTo(a.fechaMovimiento()))
                .collect(Collectors.toList());

        return new RetoDetalleDTO(
                reto.getId(),
                reto.getNombre(),
                reto.getRetoCantidad(),
                reto.getRetoCantidadActual(),
                reto.getFechaFin(),
                reto.isPredefinido(),
                movimientos);
    }

    public RetoResponse editarReto(Long id, RetoRequest request) {
        User user = getAuthenticatedUser();

        Retos reto = retoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        if (!reto.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para editar este reto");
        }

        if (request.nombre() != null) {
            reto.setNombre(request.nombre());
        }
        if (request.retoCantidad() != null) {
            reto.setRetoCantidad(request.retoCantidad());
        }
        if (request.fechaFin() != null) {
            reto.setFechaFin(request.fechaFin().atStartOfDay());
        }
        if (request.predefinido() != null) {
            reto.setPredefinido(request.predefinido());
        }

        Retos actualizado = retoRepository.save(reto);
        return RetoResponse.fromEntity(actualizado);
    }

    public RetoResponse eliminarReto(Long id) {
        User user = getAuthenticatedUser();

        Retos reto = retoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        if (!reto.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permiso para eliminar este reto");
        }

        RetoResponse eliminado = RetoResponse.fromEntity(reto);
        retoRepository.delete(reto);
        return eliminado;
    }
}