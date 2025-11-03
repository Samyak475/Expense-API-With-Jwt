package com.codeartist.expenseapi.services;

import com.codeartist.expenseapi.entity.UserEntity;
import com.codeartist.expenseapi.repository.UserRepo;
import com.codeartist.expenseapi.requestDto.AuthReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService  implements UserDetailsService{
    @Autowired
    UserRepo userRepo;
    @Override
    public UserEntity loadUserByUsername(String username)throws UsernameNotFoundException {
        return userRepo.getByUsername(username);
    }

    public  UserEntity registerUser(AuthReqDto authReqDto)throws IllegalArgumentException{

        UserEntity user = UserEntity.builder()
                .email(authReqDto.getEmail())
                .password(authReqDto.getPassword())
                .build();
        if(Objects.nonNull(loadUserByUsername(authReqDto.getEmail()))){
             throw new IllegalArgumentException();
        }
        return userRepo.save(user);
    }
}
