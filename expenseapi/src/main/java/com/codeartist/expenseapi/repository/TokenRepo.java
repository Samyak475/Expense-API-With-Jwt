package com.codeartist.expenseapi.repository;

import com.codeartist.expenseapi.entity.TokenEntity;
import com.codeartist.expenseapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<TokenEntity,Long> {
    TokenEntity findByToken(String token);
    Optional<TokenEntity> findByUser(UserEntity user);
}
