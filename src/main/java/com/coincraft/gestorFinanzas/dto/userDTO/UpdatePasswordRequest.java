package com.coincraft.gestorFinanzas.dto.userDTO;

public record UpdatePasswordRequest(
    String oldPassword,
    String newPassword,
    String newConfirmationPassword
) {}
