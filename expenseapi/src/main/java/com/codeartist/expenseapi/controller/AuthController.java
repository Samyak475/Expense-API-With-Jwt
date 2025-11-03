package com.codeartist.expenseapi.controller;

import com.codeartist.expenseapi.repository.UserRepo;
import com.codeartist.expenseapi.requestDto.AuthReqDto;
import com.codeartist.expenseapi.services.JwtService;
import com.codeartist.expenseapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.coyote.http11.Constants.a;

@RestController
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody AuthReqDto authReqDto
            ){

        authReqDto.setPassword(passwordEncoder.encode(authReqDto.getPassword()));
        try {
            userService.registerUser(authReqDto);
            String token = jwtService.generateToken(authReqDto.getEmail());
            return new ResponseEntity<>("Token for this user is : " + token, HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("User already exist: " , HttpStatus.BAD_REQUEST);
        }
    }
}
