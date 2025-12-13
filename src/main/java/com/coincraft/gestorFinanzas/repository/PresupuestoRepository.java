package com.coincraft.gestorFinanzas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coincraft.gestorFinanzas.model.Presupuesto;

/*
 * Esta interfaz es la capa que habla directamente con la tabla "presupuestos" de la BBDD.
 * 
 * Al extender de JpaRepository<Presupuesto, Long> heredamos muchos métodos ya hechos:
 *  - save(presupuesto)
 *  - findById(id)
 *  - findAll()
 *  - deleteById(id)
 * 
 * Además, añadimos un método personalizado para buscar todos los presupuestos de un usuario.
 */

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    //Buscamos todos los presupuestos asociados a un usuario por su ID

    @Query("SELECT p FROM Presupuesto p WHERE p.user_id.id= :userID")
    List<Presupuesto> findByUserId(@Param("userId") Long userId);
    

}
