package com.codeartist.expenseapi.filters;

import com.codeartist.expenseapi.entity.UserEntity;
import com.codeartist.expenseapi.services.JwtService;
import com.codeartist.expenseapi.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtService jwtService;

    public  JwtAuthFilter(UserService userService,JwtService jwtService){
        this.jwtService=jwtService;
        this.userService=userService;
    }
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String username =null;
        String token = null;
        if(header != null && header.contains("Bearer ")){
            token = header.substring(7);
            try{
               username= jwtService.getSubject(token);
            } catch (Exception e) {
                e.printStackTrace();
                filterChain.doFilter(request,response);
                return ;
            }
        }
        if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserEntity user = userService.loadUserByUsername(username);
            if(jwtService.isValidToken(token ,user)) {
                UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                token1.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token1);
            }
        }
        filterChain.doFilter(request,response);
    }
}
