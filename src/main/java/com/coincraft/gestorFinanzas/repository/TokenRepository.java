package com.coincraft.gestorFinanzas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import com.coincraft.gestorFinanzas.model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
        select t from tokens t join t.user u
        where u.id = :id and (t.isExpired = false and t.isRevoked = false)
    """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}