package com.coincraft.gestorFinanzas.service;

import java.util.List;

import com.coincraft.gestorFinanzas.dto.userDTO.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.coincraft.gestorFinanzas.dto.TokenResponse;
import com.coincraft.gestorFinanzas.model.Token;
import com.coincraft.gestorFinanzas.model.User;
import com.coincraft.gestorFinanzas.repository.TokenRepository;
import com.coincraft.gestorFinanzas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    /*

    Se comenta sólo para las pruebas. Cuando funcione el login, éste será el utilizado
    por todos los endpoints

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails details)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return userRepository.findByEmail(details.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    */

    // Para pruebas: siempre devolver el usuario con id=1
    //Debe borrarse el metodo cuando se arregle el login
    public User getAuthenticatedUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //Hardcodeado para que devuelva siempre el usuario id=1
    public UserResponse getProfileData(){
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserResponse.fromEntity(user);
    }

    public List<String> changeUsername(UpdateNameRequest request){
        User user = getAuthenticatedUser();
        String previusName = user.getName();
        user.setName(request.newName());
        userRepository.save(user);
        return List.of(previusName, user.getName());
    }

    public ChangeEmailResponse changeEmail(UpdateEmailRequest request){
        User user = getAuthenticatedUser();
        String previousEmail = user.getEmail();

        if (userRepository.existsByEmail(request.newEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email en uso");
        }

        user.setEmail(request.newEmail());
        userRepository.save(user);
        
        revokeAllUserTokens(user);
        TokenResponse tokens = reNewTokens(user);
        return new ChangeEmailResponse(previousEmail,user.getEmail(),tokens);
    }

    private void revokeAllUserTokens(final User user) {
        final List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setIsExpired(true);
                token.setIsRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public ChangePasswordResponse changePassword(UpdatePasswordRequest request){

        User user = getAuthenticatedUser();

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña actual incorrecta");
        }

        if (!request.newPassword().equals(request.newConfirmationPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña debe ser diferente");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        revokeAllUserTokens(user);
        TokenResponse tokens = reNewTokens(user);

        return new ChangePasswordResponse("Contraseña actulizada correctamente", tokens);
    }

    public ApiMessageResponse logout(String authorizationHeader){
        User user = getAuthenticatedUser();

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header inválido");
        }

        final String tokenValue = authorizationHeader.substring(7);
        Token storedToken = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token no encontrado"));

        if (!storedToken.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El token no pertenece al usuario autenticado");
        }

        storedToken.setIsExpired(true);
        storedToken.setIsRevoked(true);
        tokenRepository.save(storedToken);
        SecurityContextHolder.clearContext();

        return new ApiMessageResponse("Sesión cerrada correctamente");
    }


    private TokenResponse reNewTokens(User user){
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenRepository.save(
            Token.builder()
                .user(user)
                .token(accessToken)
                .tokenType(Token.TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build()
        );

        return new TokenResponse(accessToken, refreshToken);
    }


}
    
    

