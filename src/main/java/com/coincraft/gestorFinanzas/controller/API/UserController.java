package com.coincraft.gestorFinanzas.controller.API;

import java.util.List;

import static org.hibernate.internal.util.PropertiesHelper.map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coincraft.gestorFinanzas.dto.userDTO.UserResponse;
import com.coincraft.gestorFinanzas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;

import com.coincraft.gestorFinanzas.dto.userDTO.ApiMessageResponse;
import com.coincraft.gestorFinanzas.dto.userDTO.ChangeEmailResponse;
import com.coincraft.gestorFinanzas.dto.userDTO.UpdateEmailRequest;
import com.coincraft.gestorFinanzas.dto.userDTO.UpdateNameRequest;
import com.coincraft.gestorFinanzas.dto.userDTO.UserProfileResponse;
import com.coincraft.gestorFinanzas.service.UserService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    // Test para ver todos los usuarios creados  
    @GetMapping("/getAllUsers")
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    // Optener informacion del user Authenticado
    @GetMapping("/AuthUserData")
    public UserProfileResponse getProfileData() {
        return userService.getProfileData();
    }

    // Cambiar el nombre del usuario
    @PutMapping("/changeUserName")
    public ResponseEntity<ApiMessageResponse> changeUserName(@RequestBody @Valid UpdateNameRequest request) {
        List<String> nombres = userService.changeUsername(request);
        return ResponseEntity.ok(new ApiMessageResponse("Nombre cambiado correctamente de " + nombres.get(0) + " a " + nombres.get(1)));
    }

    //Cambiar el email del usuario
    @PutMapping("/changeUserEmail")
    public ResponseEntity<ChangeEmailResponse> changeUserEmail(
        @RequestBody @Valid UpdateEmailRequest request) {
    return ResponseEntity.ok(userService.changeEmail(request));
}
    


}
