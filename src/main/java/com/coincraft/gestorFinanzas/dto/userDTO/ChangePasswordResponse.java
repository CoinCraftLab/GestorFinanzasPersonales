package com.coincraft.gestorFinanzas.dto.userDTO;

import com.coincraft.gestorFinanzas.dto.TokenResponse;

public record ChangePasswordResponse(
    String message,
    TokenResponse tokens
) {}
