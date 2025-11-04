package com.codeartist.expenseapi.config;

import com.codeartist.expenseapi.filters.JwtAuthFilter;
import com.codeartist.expenseapi.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableWebSecurity

public class Config {
    private final UserService userService;
    public Config(UserService userService){
        this.userService=userService;
    }
    public UserDetailsService userDetailsService(){
        return new  UserService();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider dao= new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordEncoder());
        dao.setUserDetailsService(userService);
        return  dao;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return  new ProviderManager(Arrays.asList( daoAuthenticationProvider()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthFilter jwtAuthFilter) throws Exception {
        httpSecurity.authorizeHttpRequests(auth->
                auth.requestMatchers("/register","/login","/refresh").permitAll()
                .anyRequest().authenticated()
        ).csrf(csrf->csrf.disable())
        .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .authenticationProvider(daoAuthenticationProvider());
        return httpSecurity.build();
    }
}
