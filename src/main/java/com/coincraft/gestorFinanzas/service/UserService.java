package com.coincraft.gestorFinanzas.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coincraft.gestorFinanzas.model.User;
import com.coincraft.gestorFinanzas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //Register
    public User register(User user){

        //Aqui encryptamos la contraseña con el passwordEncoder seteado en el config
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Devolvemos a la funcion una query con JPA usando el userRepository y lo guardamos en la BBDD.
        return userRepository.save(user); 
    }

    //Funcion para buscar a un usuario por el email 
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);

    }
    
    
}
