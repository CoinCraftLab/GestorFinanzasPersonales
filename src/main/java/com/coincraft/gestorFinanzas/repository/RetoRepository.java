package com.coincraft.gestorFinanzas.repository;

import com.coincraft.gestorFinanzas.model.Retos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetoRepository extends JpaRepository<Retos, Long> {

    //Busca los retos del usuario
    List<Retos> findByUserId(Long userId);
}
