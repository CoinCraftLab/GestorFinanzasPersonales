package com.coincraft.gestorFinanzas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coincraft.gestorFinanzas.model.Transaccion;

public interface TransactionRepository extends JpaRepository<Transaccion, Long>{
    List<Transaccion> findByUserIdOrderByFechaTransaccionDesc(Long userId);
    
}
