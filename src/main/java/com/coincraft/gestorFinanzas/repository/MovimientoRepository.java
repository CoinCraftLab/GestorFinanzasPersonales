package com.coincraft.gestorFinanzas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coincraft.gestorFinanzas.model.MovimientoReto;

public interface MovimientoRepository extends JpaRepository<MovimientoReto, Long> {

    //Busca los movimientos de un reto específico
    List<MovimientoReto> findByRetoId(Long reto_id);
}
