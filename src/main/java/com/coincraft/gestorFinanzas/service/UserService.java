package com.coincraft.gestorFinanzas.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.coincraft.gestorFinanzas.dto.TokenResponse;
import com.coincraft.gestorFinanzas.dto.userDTO.ChangeEmailResponse;
import com.coincraft.gestorFinanzas.dto.userDTO.UpdateEmailRequest;
import com.coincraft.gestorFinanzas.dto.userDTO.UpdateNameRequest;
import com.coincraft.gestorFinanzas.dto.userDTO.UserProfileResponse;
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
    private final AuthService authService;

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails details)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return userRepository.findByEmail(details.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserProfileResponse getProfileData(){
        User user = getAuthenticatedUser();
        return new UserProfileResponse(user.getName(),user.getEmail());
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
        String access_token = jwtService.generateToken(user);
        String refresh_token = jwtService.generateRefreshToken(user);

        return new ChangeEmailResponse(previousEmail,user.getEmail(),new TokenResponse(access_token, refresh_token));
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
}
    
    

