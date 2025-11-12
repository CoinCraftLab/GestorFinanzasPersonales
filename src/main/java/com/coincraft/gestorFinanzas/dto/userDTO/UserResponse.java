package com.coincraft.gestorFinanzas.dto.userDTO;

import com.coincraft.gestorFinanzas.model.User;
    
    public record UserResponse(Long id, String name, String email) {
        
    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
