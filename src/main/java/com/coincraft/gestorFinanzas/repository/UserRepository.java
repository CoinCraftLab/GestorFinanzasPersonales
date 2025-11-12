package com.coincraft.gestorFinanzas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coincraft.gestorFinanzas.model.User;


/* Con esta interfaz vienen incluidos los siguientes metodos de manera automatica 
    -userRepository.save(user);
    -userRepository.findById(1L);
    -userRepository.findAll();
    -userRepository.deleteById(1L);


    Ejemplos de como Spring puede interpretar los nombres de las funciones 
        findBy → buscar
        countBy → contar
        existsBy → comprobar existencia
        deleteBy → borrar
        OrderBy → ordenar resultados

    Ejemplos practicos
        List<User> findByName(String name); // SELECT * FROM users WHERE name = ?
        List<User> findByNameContaining(String text); // LIKE %text%
        List<User> findByEmailEndingWith(String domain); // email LIKE %domain
        List<User> findByNameAndEmail(String name, String email); // AND
        List<User> findByNameOrEmail(String name, String email); // OR
        List<User> findByIdGreaterThan(Long id); // id > ?
        List<User> findByNameOrderByEmailAsc(String name); // ORDER BY email ASC
*/
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);


}