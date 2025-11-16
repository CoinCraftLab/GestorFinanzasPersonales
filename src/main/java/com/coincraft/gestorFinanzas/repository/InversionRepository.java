package com.coincraft.gestorFinanzas.repository;

import com.coincraft.gestorFinanzas.model.Inversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InversionRepository extends JpaRepository<Inversion, Long> {

    //Busca todas las inversiones de un usuario
    List<Inversion> findByUserId(Long userId);

    //Busca las inversiones de un usuario por activo financiero específico
    List<Inversion> findByUserIdAndActivoFinancieroId(Long userId, Long activoFinancieroId);

    //Busca las inversiones por activo financiero (para todos los usuarios)
    List<Inversion> findByActivoFinancieroId(Long activoFinancieroId);

    //Busca las inversiones por tipo (compra/venta) de un usuario
    List<Inversion> findByUserIdAndTipo(Long userId, Boolean tipo);
}
