package com.coincraft.gestorFinanzas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coincraft.gestorFinanzas.model.Retos;

public interface RetoRepository extends JpaRepository<Retos, Long> {

    //Busca los retos del usuario
    List<Retos> findByUserId(Long user_id);
}
