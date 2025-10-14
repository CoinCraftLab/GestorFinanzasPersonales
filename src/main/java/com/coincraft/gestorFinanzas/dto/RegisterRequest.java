package com.coincraft.gestorFinanzas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String name;
    private String password;
}
