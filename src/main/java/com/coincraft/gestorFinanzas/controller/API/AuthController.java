package com.coincraft.gestorFinanzas.controller.API;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coincraft.gestorFinanzas.dto.LoginRequest;
import com.coincraft.gestorFinanzas.dto.RegisterRequest;
import com.coincraft.gestorFinanzas.model.User;
import com.coincraft.gestorFinanzas.service.JwtService;
import com.coincraft.gestorFinanzas.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    //Register
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        
        User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(request.getPassword())
            .build();
        
        return ResponseEntity.ok(userService.register(user));
    }

    //Login
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        //Buscamos al usuario por el email
        User user = userService.findByEmail(request.getEmail());

        //Comprobamos si coincide el user con algun email de la bbdd
        if (user == null){
            return ResponseEntity.status(401).body("El usuario no ha sido encontrado");
        }

        //Si se ha encontrado el usuario continuamos comprobando la contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return ResponseEntity.status(401).body("La contraseña no es correcta");
        }

        String token = jwtService.generateToken(user.getEmail());
        
        return ResponseEntity.ok(Map.of(
            "message", "Login Correcto",
            "token", token
        ));
    }

    //Logout
    @PostMapping("logout")
    public ResponseEntity<?> logout() {

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return ResponseEntity.ok("Logout exitoso. Por favor borra el token en el cliente.");
    }
    
    
    
}
