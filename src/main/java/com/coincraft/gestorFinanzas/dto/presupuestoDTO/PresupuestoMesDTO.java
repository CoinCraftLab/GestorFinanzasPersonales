package com.coincraft.gestorFinanzas.dto.presupuestoDTO;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class PresupuestoMesDTO {
    Long id;
    String nombre;
    List<String> categorias;
    Double presupuestoMensual;
    Double gastadoMes; 
    Integer pctUsado;
}
