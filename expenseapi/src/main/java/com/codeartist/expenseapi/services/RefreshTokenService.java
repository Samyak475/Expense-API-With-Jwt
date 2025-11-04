package com.codeartist.expenseapi.services;

import com.codeartist.expenseapi.entity.TokenEntity;
import com.codeartist.expenseapi.entity.UserEntity;
import com.codeartist.expenseapi.repository.TokenRepo;
import com.codeartist.expenseapi.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    TokenRepo tokenRepo;
@Autowired
    UserRepo userRepo;
private final Date Refresh_Token_Time=new Date(System.currentTimeMillis()+60*60*24*7*1000L);
@Transactional
    public TokenEntity generateRefreshToken(String username) throws IllegalArgumentException{
        UserEntity user= userRepo.findByUsername(username);
        Optional<TokenEntity> tokenOptional = tokenRepo.findByUser(user);
        if(tokenOptional.isPresent()){
            TokenEntity tokenEntity = tokenOptional.get();
            tokenEntity.setExpirationTime(Refresh_Token_Time);
            return tokenRepo.save(tokenEntity);
        }

        TokenEntity token = TokenEntity.builder()
                .expirationTime(Refresh_Token_Time)
                .token(UUID.randomUUID().toString())
                .user(user)
                .build();
        return tokenRepo.save(token);
    }
    public TokenEntity findByToken(String token){
     return   tokenRepo.findByToken(token);
    }
    @Transactional
    public TokenEntity  verifyExpiration(TokenEntity token){
        if(token.getExpirationTime().before(new Date(System.currentTimeMillis()))){
            tokenRepo.delete(token);
            throw  new RuntimeException(token.getToken()+"Refresh Token expired pls login again");
        }
        return token;
    }

}
