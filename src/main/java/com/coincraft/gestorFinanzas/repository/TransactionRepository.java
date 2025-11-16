package com.coincraft.gestorFinanzas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.coincraft.gestorFinanzas.model.Transaccion;

public interface TransactionRepository extends JpaRepository<Transaccion, Long>{

    
}
