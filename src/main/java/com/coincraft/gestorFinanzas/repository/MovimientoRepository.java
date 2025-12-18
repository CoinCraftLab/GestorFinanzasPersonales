package com.coincraft.gestorFinanzas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coincraft.gestorFinanzas.model.MovimientoReto;

public interface MovimientoRepository extends JpaRepository<MovimientoReto, Long> {
    List<MovimientoReto> findByCategoriaOrigenIdOrCategoriaDestinoId(Long origenId, Long destinoId);
}
