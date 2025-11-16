package com.coincraft.gestorFinanzas.dto.inversionDTO;

import java.util.List;

public record PortfolioResponse (Double balanceTotal, List<ActivoResumenDTO> activos) {

}
