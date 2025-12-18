package com.coincraft.gestorFinanzas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coincraft.gestorFinanzas.model.Transaccion;

public interface TransactionRepository extends JpaRepository<Transaccion, Long>{
    List<Transaccion> findByUserIdOrderByFechaTransaccionDesc(Long userId);
    List<Transaccion> findByUserIdAndTipoTransferencia_NameIgnoreCaseAndFechaTransaccionBetweenOrderByFechaTransaccionDesc(
        Long userId,
        String tipo,
        LocalDate from,
        LocalDate to);

}
