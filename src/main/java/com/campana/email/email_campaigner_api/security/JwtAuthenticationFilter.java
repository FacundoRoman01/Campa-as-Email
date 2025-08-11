package com.campana.email.email_campaigner_api.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
                                    throws ServletException, IOException {
        
        System.out.println("--- JwtAuthenticationFilter está corriendo ---");
        
        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No se encontró token o el formato es inválido. Dejando pasar la petición.");
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);
        System.out.println("Token extraído: " + token);
        
        try {
            username = jwtUtils.extractUsername(token);
            System.out.println("Username extraído del token: " + username);
        } catch (Exception e) {
            System.out.println("Error al extraer el username del token: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Usuario no autenticado en el contexto. Intentando autenticar...");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            if (jwtUtils.isTokenValid(token, userDetails)) {
                System.out.println("Token es válido. Creando objeto de autenticación.");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, 
                    userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Autenticación establecida en el contexto de seguridad.");
            } else {
                System.out.println("Token no es válido.");
            }
        }
        
        System.out.println("--- Fin de JwtAuthenticationFilter ---");
        filterChain.doFilter(request, response);
    }
}