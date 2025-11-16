package com.coincraft.gestorFinanzas.repository;

import com.coincraft.gestorFinanzas.model.ActivoFinanciero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivoFinancieroRepository extends JpaRepository<ActivoFinanciero, Long> {
    //Busca activo por ticker
    Optional<ActivoFinanciero> findByTicker(String ticker);

    //Verificar si existe un activo con ese ticker
    boolean existsByTicker(String ticker);

    //Busca activos por categoría
    List<ActivoFinanciero> findByCategoriaActivoFinancieroId(Long categoriaId);

    //Busca por nombre completo
    Optional<ActivoFinanciero> findByNombreCompleto(String nombreCompleto);
}
