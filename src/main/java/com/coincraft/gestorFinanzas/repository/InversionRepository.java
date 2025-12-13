package com.coincraft.gestorFinanzas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coincraft.gestorFinanzas.model.Inversion;

public interface InversionRepository extends JpaRepository<Inversion, Long> {

    //Busca todas las inversiones de un usuario
    List<Inversion> findByUserId(Long user_id);

    //Busca las inversiones de un usuario por activo financiero específico
    List<Inversion> findByUserIdAndActivoFinancieroId(Long user_id, Long activo_financiero_id);

    //Busca las inversiones por activo financiero (para todos los usuarios)
    List<Inversion> findByActivoFinancieroId(Long activo_financiero_id);

    //Busca las inversiones por tipo (compra/venta) de un usuario
    List<Inversion> findByUserIdAndTipo(Long user_id, Boolean tipo);
}
