package com.coincraft.gestorFinanzas.repository;

import com.coincraft.gestorFinanzas.model.MovimientoReto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<MovimientoReto, Long> {

    //Busca los movimientos de un reto específico
    List<MovimientoReto> findByRetoId(Long retoId);
}
