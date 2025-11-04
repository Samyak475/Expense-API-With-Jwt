package com.codeartist.expenseapi.controller;

import com.codeartist.expenseapi.entity.TokenEntity;
import com.codeartist.expenseapi.repository.UserRepo;
import com.codeartist.expenseapi.requestDto.AuthReqDto;
import com.codeartist.expenseapi.response.JwtResponseDto;
import com.codeartist.expenseapi.services.JwtService;
import com.codeartist.expenseapi.services.RefreshTokenService;
import com.codeartist.expenseapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    RefreshTokenService refreshTokenService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody AuthReqDto authReqDto
            ){

        authReqDto.setPassword(passwordEncoder.encode(authReqDto.getPassword()));
        try {
            userService.registerUser(authReqDto);
            String token = jwtService.generateToken(authReqDto.getUsername());
            TokenEntity refreshToken = refreshTokenService.generateRefreshToken(authReqDto.getUsername());
            return new ResponseEntity<>("Token for this user is : " + token +"\n" +
                    "RefreshToken for this user is "+refreshToken.getToken(), HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("User already exist: " , HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody AuthReqDto authReqDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authReqDto.getUsername(),authReqDto.getPassword())
        );
        if(authentication.isAuthenticated()){
            String accessToken = jwtService.generateToken(authReqDto.getUsername());
            try {
                TokenEntity refreshToken = refreshTokenService.generateRefreshToken(authReqDto.getUsername());

                return new ResponseEntity(JwtResponseDto.builder()
                        .accessToken(accessToken).refreshToken(refreshToken.getToken()).build()
                        , HttpStatus.OK);
            } catch (Exception e) {
                return     new ResponseEntity<>("user Not authenticated",HttpStatus.BAD_REQUEST);
            }
        }
        return     new ResponseEntity<>("user Not authenticated",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(@RequestBody  String token){
        TokenEntity tokenEntity = refreshTokenService.findByToken(token);
        if(tokenEntity == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        TokenEntity validToken = refreshTokenService.verifyExpiration(tokenEntity);
        String accessToken = jwtService.generateToken(validToken.getUser().getUsername());
        return new ResponseEntity(JwtResponseDto.builder().accessToken(accessToken).refreshToken(validToken.getToken()).build(),
                HttpStatus.FOUND);
    }
}
