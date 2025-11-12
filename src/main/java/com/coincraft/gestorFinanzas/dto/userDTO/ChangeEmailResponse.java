package com.coincraft.gestorFinanzas.dto.userDTO;

import com.coincraft.gestorFinanzas.dto.TokenResponse;

public record ChangeEmailResponse(
        String previousEmail,
        String newEmail,
        TokenResponse tokens
) {}