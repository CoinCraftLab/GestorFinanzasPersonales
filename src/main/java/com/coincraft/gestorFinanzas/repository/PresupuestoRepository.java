package com.coincraft.gestorFinanzas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coincraft.gestorFinanzas.model.Presupuesto;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    @Query("SELECT p FROM Presupuesto p WHERE p.user_id.id = :userId")
    List<Presupuesto> findByUserId(@Param("userId") Long userId);

}